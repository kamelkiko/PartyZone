package com.app.partyzone.core.data.repository.remote

import android.net.Uri
import com.app.partyzone.core.domain.entity.Favorite
import com.app.partyzone.core.domain.entity.ItemType
import com.app.partyzone.core.domain.entity.Notification
import com.app.partyzone.core.domain.entity.NotificationType
import com.app.partyzone.core.domain.entity.Request
import com.app.partyzone.core.domain.entity.SearchResult
import com.app.partyzone.core.domain.entity.UpdateUser
import com.app.partyzone.core.domain.entity.User
import com.app.partyzone.core.domain.repository.UserRepository
import com.app.partyzone.core.domain.util.AuthorizationException
import com.app.partyzone.core.domain.util.UnknownErrorException
import com.app.partyzone.core.util.isNotEmptyAndBlank
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
) : UserRepository {

    override suspend fun getCurrentUser(): User {
        val userAuthId = firebaseAuth.currentUser?.uid
        val userDocument = firestore.collection("users")
            .document(userAuthId ?: "")
            .get()
            .await()

        return User(
            id = userDocument.get("id").toString(),
            name = userDocument.get("name").toString(),
            email = userDocument.get("email").toString(),
            photoUrl = userDocument.get("photoUrl").toString(),
        )
    }

    override suspend fun getUserById(id: String): User {
        val userDocument = firestore.collection("users")
            .document(id)
            .get()
            .await()

        return User(
            id = userDocument.get("id").toString(),
            name = userDocument.get("name").toString(),
            email = userDocument.get("email").toString(),
            photoUrl = userDocument.get("photoUrl").toString(),
        )
    }

    override suspend fun updateCurrentUser(user: UpdateUser) {
        val currentUser =
            firebaseAuth.currentUser ?: throw AuthorizationException.UnAuthorizedException

        val credential =
            EmailAuthProvider.getCredential(currentUser.email ?: "", user.oldPassword)
        try {
            currentUser.reauthenticate(credential).await()
        } catch (e: Exception) {
            throw AuthorizationException.InvalidPasswordException
        }

        try {
            if (user.email != currentUser.email) {
                currentUser.verifyBeforeUpdateEmail(user.email).await()
            }
            if (user.newPassword.isNotEmptyAndBlank()) {
                currentUser.updatePassword(user.newPassword).await()
            }
        } catch (e: Exception) {
            throw UnknownErrorException("Failed to update your profile: ${e.message}")
        }

        var photoUrl: String? = null
        if (user.imageUri != null) {
            // Upload the new photo to Firebase Storage
            val fileUri = Uri.parse(user.photoUrl)
            val storageRef = firebaseStorage.reference
            val photoRef = storageRef.child("profile_images/${currentUser.uid}.jpg")
            try {
                photoRef.putFile(fileUri).await()
                photoUrl = photoRef.downloadUrl.await().toString()
            } catch (e: Exception) {
                throw UnknownErrorException("Failed to upload photo: ${e.message}")
            }
        }

        val userRef = firestore.collection("users").document(currentUser.uid)
        val updates = hashMapOf<String, Any>(
            "name" to user.name,
            "email" to user.email
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

    override suspend fun addToFavorites(favorite: Favorite) {
        firestore.collection("favorites")
            .document(favorite.id)
            .set(favorite.copy(userId = firebaseAuth.currentUser?.uid ?: ""))
            .await()
        val notification = Notification(
            id = UUID.randomUUID().toString(),
            sellerId = favorite.sellerId,
            type = NotificationType.Favorite.name,
            message = "A user has added you to their favorites.",
            userId = "",
            timeStamp = Timestamp.now()
        )
        sendNotification(notification)
    }

    override suspend fun removeFromFavorites(favoriteId: String) {
        val notification = Notification(
            id = UUID.randomUUID().toString(),
            sellerId = firestore.collection("favorites")
                .document(favoriteId)
                .get()
                .await()
                .get("sellerId").toString(),
            type = NotificationType.Unfavorite.name,
            message = "A user has removed you from their favorites.",
            userId = "",
            timeStamp = Timestamp.now()
        )

        firestore.collection("favorites")
            .document(favoriteId)
            .delete()
            .await()

        sendNotification(notification)
    }

    override suspend fun getFavorites(): List<Favorite> {
        val favorites = mutableListOf<Favorite>()
        val data = firestore.collection("favorites")
            .whereEqualTo("userId", firebaseAuth.currentUser?.uid ?: "")
            .get()
            .await()
        data.forEach {
            favorites.add(
                Favorite(
                    id = it.get("id").toString(),
                    userId = it.get("userId").toString(),
                    type = it.get("type").toString(),
                    itemId = it.get("itemId").toString(),
                    name = it.get("name").toString(),
                    location = it.get("location").toString(),
                    imageUrl = it.get("imageUrl").toString(),
                    price = it.get("price").toString().toDoubleOrNull(),
                    sellerId = it.get("sellerId").toString()
                )
            )
        }
        return favorites.sortedBy { it.name }
    }

    override suspend fun getNotifications(): List<Notification> {
        val notifications = mutableListOf<Notification>()
        val data = firestore.collection("notifications")
            .whereEqualTo("userId", firebaseAuth.currentUser?.uid ?: "")
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
                val notificationRef = firestore
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
            .set(notification)
            .await()
    }

    override fun hasNotification(): Flow<Boolean> = callbackFlow {
        val listener = firestore.collection("notifications")
            .whereEqualTo("userId", firebaseAuth.currentUser?.uid ?: "")
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

    override suspend fun searchAll(query: String): List<SearchResult> {
        try {
            if (query.isNotEmptyAndBlank().not()) return emptyList()

            val result = mutableListOf<SearchResult>()

            val favoritesSnapshot = firestore.collection("favorites")
                .whereEqualTo("userId", firebaseAuth.currentUser?.uid ?: "")
                .get()
                .await()

            val favoriteMap = favoritesSnapshot.documents.associate {
                (it.getString("itemId") ?: "") to it.id
            }

            val sellersQuery = firestore.collection("sellers")
                .whereGreaterThanOrEqualTo("name", query)
                .whereLessThanOrEqualTo("name", query + "\uf8ff")

            val postsQuery = firestore.collection("Post")
                .whereGreaterThanOrEqualTo("description", query)
                .whereLessThanOrEqualTo("description", query + "\uf8ff")

            val offersQuery = firestore.collection("offers")
                .whereGreaterThanOrEqualTo("description", query)
                .whereLessThanOrEqualTo("description", query + "\uf8ff")

            // Perform all queries in parallel
            val sellersTask = sellersQuery.get()
            val postsTask = postsQuery.get()
            val offersTask = offersQuery.get()

            val querySnapshots =
                Tasks.whenAllSuccess<QuerySnapshot>(sellersTask, postsTask, offersTask).await()

            if (querySnapshots[0] != null) {
                querySnapshots[0].forEach { userDocument ->
                    result.add(
                        SearchResult(
                            id = userDocument.get("id").toString(),
                            name = userDocument.get("name").toString(),
                            location = userDocument.get("location").toString(),
                            imageUrl = userDocument.get("photoUrl").toString(),
                            itemId = userDocument.get("id").toString(),
                            type = ItemType.Seller.name,
                            isFav = favoriteMap.containsKey(userDocument.get("id").toString()),
                            favId = favoriteMap[userDocument.get("id").toString()],
                            sellerId = userDocument.get("id").toString(),
                        )
                    )
                }
            }

            if (querySnapshots[1] != null) {
                querySnapshots[1].forEach { userDocument ->
                    result.add(
                        SearchResult(
                            id = userDocument.get("id").toString(),
                            name = userDocument.get("description").toString(),
                            location = userDocument.get("category").toString(),
                            imageUrl = userDocument.get("imageUrl").toString(),
                            itemId = userDocument.get("id").toString(),
                            type = ItemType.Post.name,
                            isFav = favoriteMap.containsKey(userDocument.get("id").toString()),
                            favId = favoriteMap[userDocument.get("id").toString()],
                            sellerId = userDocument.get("sellerId").toString(),
                        )
                    )
                }
            }

            if (querySnapshots[2] != null) {
                querySnapshots[2].forEach { userDocument ->
                    result.add(
                        SearchResult(
                            id = userDocument.get("id").toString(),
                            name = userDocument.get("description").toString(),
                            location = userDocument.get("category").toString(),
                            imageUrl = userDocument.get("imageUrl").toString(),
                            itemId = userDocument.get("id").toString(),
                            type = ItemType.Offer.name,
                            price = userDocument.get("price").toString().toDoubleOrNull() ?: 0.0,
                            isFav = favoriteMap.containsKey(userDocument.get("id").toString()),
                            favId = favoriteMap[userDocument.get("id").toString()],
                            sellerId = userDocument.get("sellerId").toString(),
                        )
                    )
                }
            }

            return result
        } catch (e: Exception) {
            throw UnknownErrorException(e.message.toString())
        }
    }

    override suspend fun sendRequest(request: Request) {
        firestore.collection("requests")
            .document(request.id)
            .set(request)
            .await()

        val notification = Notification(
            id = UUID.randomUUID().toString(),
            sellerId = request.sellerId,
            type = NotificationType.Request.name,
            message = "A user has requested you for rent.",
            userId = "",
            timeStamp = Timestamp.now()
        )

        sendNotification(notification)
    }

    override suspend fun cancelRequest(requestId: String) {
        firestore.collection("requests")
            .document(requestId)
            .update("status", "Cancelled")
            .await()

        val notification = Notification(
            id = UUID.randomUUID().toString(),
            sellerId = firestore.collection("requests")
                .document(requestId)
                .get()
                .await()
                .get("sellerId").toString(),
            type = NotificationType.Cancel.name,
            message = "A user has cancelled the request",
            userId = "",
            timeStamp = Timestamp.now()
        )

        sendNotification(notification)
    }

    override suspend fun fetchUserRequests(): List<Request> {
        val requests = mutableListOf<Request>()

        val data = firestore.collection("requests")
            .whereEqualTo("userId", firebaseAuth.currentUser?.uid ?: "")
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
        return requests.sortedByDescending { it.createdAt }
    }
}