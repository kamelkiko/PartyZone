package com.app.partyzone.core.data.repository.remote

import android.net.Uri
import com.app.partyzone.core.domain.entity.Notification
import com.app.partyzone.core.domain.entity.Request
import com.app.partyzone.core.domain.entity.Seller
import com.app.partyzone.core.domain.entity.UpdateSeller
import com.app.partyzone.core.domain.repository.SellerRepository
import com.app.partyzone.core.domain.util.AuthorizationException
import com.app.partyzone.core.domain.util.UnknownErrorException
import com.app.partyzone.core.util.isNotEmptyAndBlank
import com.google.firebase.Timestamp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class SellerRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
) : SellerRepository {

    override suspend fun getCurrentSeller(): Seller {
        val userAuthId = firebaseAuth.currentUser?.uid
        val userDocument = firestore.collection("sellers")
            .document(userAuthId ?: "")
            .get()
            .await()

        return Seller(
            id = userDocument.get("id").toString(),
            name = userDocument.get("name").toString(),
            email = userDocument.get("email").toString(),
            photoUrl = userDocument.get("photoUrl").toString(),
            location = userDocument.get("location").toString(),
            description = userDocument.get("description").toString(),
            contactInfo = userDocument.get("contactInfo").toString(),
        )
    }

    override suspend fun getSellerById(id: String): Seller {
        val userDocument = firestore.collection("sellers")
            .document(id)
            .get()
            .await()

        return Seller(
            id = userDocument.get("id").toString(),
            name = userDocument.get("name").toString(),
            email = userDocument.get("email").toString(),
            photoUrl = userDocument.get("photoUrl").toString(),
            location = userDocument.get("location").toString(),
            description = userDocument.get("description").toString(),
            contactInfo = userDocument.get("contactInfo").toString(),
        )
    }

    override suspend fun updateCurrentSeller(seller: UpdateSeller) {
        val currentUser =
            firebaseAuth.currentUser ?: throw AuthorizationException.UnAuthorizedException

        val credential =
            EmailAuthProvider.getCredential(currentUser.email ?: "", seller.oldPassword)
        try {
            currentUser.reauthenticate(credential).await()
        } catch (e: Exception) {
            throw AuthorizationException.InvalidPasswordException
        }

        try {
            if (seller.email != currentUser.email) {
                currentUser.verifyBeforeUpdateEmail(seller.email).await()
            }
            if (seller.newPassword.isNotEmptyAndBlank()) {
                currentUser.updatePassword(seller.newPassword).await()
            }
        } catch (e: Exception) {
            throw UnknownErrorException("Failed to update your profile: ${e.message}")
        }

        var photoUrl: String? = seller.photoUrl
        if (seller.photoUrl != null && seller.photoUrl.startsWith("file:")) {
            // Upload the new photo to Firebase Storage
            val fileUri = Uri.parse(seller.photoUrl)
            val storageRef = firebaseStorage.reference
            val photoRef = storageRef.child("profile_images/${currentUser.uid}.jpg")
            try {
                photoRef.putFile(fileUri).await()
                photoUrl = photoRef.downloadUrl.await().toString()
            } catch (e: Exception) {
                throw UnknownErrorException("Failed to upload photo: ${e.message}")
            }
        }

        val userRef = firestore.collection("sellers").document(currentUser.uid)
        val updates = hashMapOf<String, Any>(
            "name" to seller.name,
            "email" to seller.email
        )
        if (photoUrl != null) {
            updates["photoUrl"] = photoUrl
        }
        try {
            userRef.update(updates).await()
        } catch (e: Exception) {
            throw UnknownErrorException("Failed to update your profile: ${e.message}")
        }
    }

    override suspend fun getNotifications(): List<Notification> {
        val notifications = mutableListOf<Notification>()
        val data = firestore.collection("notifications")
            .whereEqualTo("sellerId", firebaseAuth.currentUser?.uid ?: "")
            .get()
            .await()
        data.forEach {
            notifications.add(
                Notification(
                    id = it.get("id").toString(),
                    userId = it.get("userId").toString(),
                    sellerId = it.get("sellerId").toString(),
                    type = it.get("type").toString(),
                    message = it.get("message").toString(),
                    isRead = it.get("isRead").toString().toBoolean(),
                    timeStamp = it.getTimestamp("timeStamp")
                        ?: throw UnknownErrorException("Timestamp is null")
                )
            )
        }
        if (notifications.isNotEmpty()) {
            val batch = firestore.batch()
            for (notification in notifications) {
                val notificationRef = FirebaseFirestore.getInstance()
                    .collection("notifications")
                    .document(notification.id)
                batch.update(notificationRef, "isRead", true)
            }
            batch.commit().await()
        }
        return notifications.sortedByDescending { it.timeStamp }
    }

    override suspend fun sendNotification(notification: Notification) {
        firestore.collection("notifications")
            .document(notification.id)
            .set(notification.copy(userId = firebaseAuth.currentUser?.uid ?: ""))
            .await()
    }

    override fun hasNotification(): Flow<Boolean> = callbackFlow {
        val listener = firestore.collection("notifications")
            .whereEqualTo("sellerId", firebaseAuth.currentUser?.uid ?: "")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    close(error) // Close the flow with an error
                    throw UnknownErrorException(error.message.toString())
                }

                val hasUnreadNotification = value?.any {
                    it.get("isRead").toString().toBoolean().not()
                } ?: false

                trySend(hasUnreadNotification) // Emit the result to the flow
            }

        // This block is called when the flow is cancelled or completed
        awaitClose {
            listener.remove() // Remove the listener when the flow is no longer needed
        }
    }

    override suspend fun acceptRequest(requestId: String) {
        firestore.collection("requests")
            .document(requestId)
            .update("status", "Completed")
            .await()

        val notification = Notification(
            id = UUID.randomUUID().toString(),
            userId = firestore.collection("requests")
                .document(requestId)
                .get()
                .await()
                .get("userId").toString(),
            type = "Request",
            message = "A user has accepted your request",
            sellerId = "",
            timeStamp = Timestamp.now()
        )

        firestore.collection("notifications")
            .document(notification.id)
            .set(notification)
            .await()
    }

    override suspend fun cancelRequest(requestId: String) {
        firestore.collection("requests")
            .document(requestId)
            .update("status", "Cancelled")
            .await()

        val notification = Notification(
            id = UUID.randomUUID().toString(),
            userId = firestore.collection("requests")
                .document(requestId)
                .get()
                .await()
                .get("userId").toString(),
            type = "Cancel",
            message = "A seller has cancelled the request",
            sellerId = "",
            timeStamp = Timestamp.now()
        )

        firestore.collection("notifications")
            .document(notification.id)
            .set(notification)
            .await()
    }

    override suspend fun fetchSellerRequests(): List<Request> {
        val requests = mutableListOf<Request>()

        val data = firestore.collection("requests")
            .whereEqualTo("sellerId", firebaseAuth.currentUser?.uid ?: "")
            .get()
            .await()

        data.forEach { request ->
            requests.add(
                Request(
                    id = request.get("id").toString(),
                    userId = request.get("userId").toString(),
                    sellerId = request.get("sellerId").toString(),
                    status = request.get("status").toString(),
                    itemId = request.get("itemId").toString(),
                    itemImageUrl = request.get("itemImageUrl").toString(),
                    itemName = request.get("itemName").toString(),
                    type = request.get("type").toString(),
                    createdAt = request.getTimestamp("createdAt")
                        ?: throw UnknownErrorException("TimeStamp is null"),
                )
            )
        }
        return requests
    }
}