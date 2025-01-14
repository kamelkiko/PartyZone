package com.app.partyzone.core.data.repository.remote

import com.app.partyzone.core.domain.entity.Favorite
import com.app.partyzone.core.domain.entity.Notification
import com.app.partyzone.core.domain.entity.Seller
import com.app.partyzone.core.domain.entity.User
import com.app.partyzone.core.domain.repository.UserRepository
import com.app.partyzone.core.domain.util.UnknownErrorException
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
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

    override suspend fun updateCurrentUser(user: User) {

    }

    override suspend fun addToFavorites(favorite: Favorite) {
        firestore.collection("favorites")
            .document(favorite.id)
            .set(favorite.copy(userId = firebaseAuth.currentUser?.uid ?: ""))
            .await()
        val notification = Notification(
            id = UUID.randomUUID().toString(),
            sellerId = favorite.sellerId,
            type = "Favorite",
            message = "A user has added you to their favorites.",
            userId = "",
            date = Timestamp.now().toDate().toString()
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
            type = "Unfavorite",
            message = "A user has removed you from their favorites.",
            userId = "",
            date = Timestamp.now().toDate().toString()
        )

        firestore.collection("favorites")
            .document(favoriteId)
            .delete()
            .await()

        firestore.collection("notifications")
            .document(notification.id)
            .set(notification)
            .await()
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
        return favorites
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

    override suspend fun searchSellers(query: String): List<Seller> {
        val sellers = mutableListOf<Seller>()

        val data = firestore.collection("sellers")
            .whereGreaterThanOrEqualTo("name", query)
            .whereLessThanOrEqualTo("name", query + "\uf8ff")
            .get()
            .await()
        data.forEach { userDocument ->
            sellers.add(
                Seller(
                    id = userDocument.get("id").toString(),
                    name = userDocument.get("name").toString(),
                    email = userDocument.get("email").toString(),
                    photoUrl = userDocument.get("photoUrl").toString(),
                    location = userDocument.get("location").toString(),
                    description = userDocument.get("description").toString(),
                    contactInfo = userDocument.get("contactInfo").toString(),
                )
            )
        }

        return sellers
    }
}