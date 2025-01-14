package com.app.partyzone.core.data.repository.remote

import android.util.Log
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
            favouriteSellers = userDocument.get("favouriteSellers") as? List<String> ?: emptyList()
        ).apply { Log.i("KIKO", this.toString()) }
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
            favouriteSellers = userDocument.get("favouriteSellers") as? List<String> ?: emptyList()
        )
    }

    override suspend fun updateCurrentUser(user: User) {

    }
}