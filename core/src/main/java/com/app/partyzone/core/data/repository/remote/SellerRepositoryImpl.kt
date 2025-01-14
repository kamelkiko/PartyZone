package com.app.partyzone.core.data.repository.remote

import com.app.partyzone.core.domain.entity.Notification
import com.app.partyzone.core.domain.entity.Seller
import com.app.partyzone.core.domain.repository.SellerRepository
import com.app.partyzone.core.domain.util.UnknownErrorException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SellerRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
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

    override suspend fun updateCurrentSeller(seller: Seller) {

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
                    date = it.getTimestamp("timestamp")?.toDate()?.toString()
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
        return notifications
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
}