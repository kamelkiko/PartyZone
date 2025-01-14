package com.app.partyzone.core.data.repository.remote

import com.app.partyzone.core.domain.entity.Favorite
import com.app.partyzone.core.domain.entity.User
import com.app.partyzone.core.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
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
    }

    override suspend fun removeFromFavorites(favoriteId: String) {
        firestore.collection("favorites")
            .document(favoriteId)
            .delete()
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
                    price = it.get("price").toString().toDoubleOrNull()
                )
            )
        }
        return favorites
    }
}