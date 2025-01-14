package com.app.partyzone.core.data.repository.remote

import com.app.partyzone.core.domain.entity.Seller
import com.app.partyzone.core.domain.entity.User
import com.app.partyzone.core.domain.repository.AuthRepository
import com.app.partyzone.core.domain.util.AuthorizationException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore
) : AuthRepository {
    override suspend fun loginUser(email: String, password: String): Boolean {
        return try {
            val user = firebaseAuth.signInWithEmailAndPassword(email, password).await().user
                ?: throw AuthorizationException.UserNotFoundException("User not found")
            val client = firebaseFireStore.collection("users")
                .document(user.uid)
                .get().await()
            if (client.exists().not()) {
                throw AuthorizationException.UserNotFoundException("User not found")
            }
            true
        } catch (e: Exception) {
            throw Exception("Error logging in: ${e.message}")
        }
    }

    override suspend fun loginSeller(email: String, password: String): Boolean {
        return try {
            val user = firebaseAuth.signInWithEmailAndPassword(email, password).await().user
                ?: throw AuthorizationException.UserNotFoundException("User not found")
            val client = firebaseFireStore.collection("sellers")
                .document(user.uid)
                .get().await()
            if (client.exists().not()) {
                throw AuthorizationException.UserNotFoundException("User not found")
            }
            true
        } catch (e: Exception) {
            throw Exception("Error logging in: ${e.message}")
        }
    }

    override suspend fun signupUser(email: String, password: String, userName: String): Boolean {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            firebaseAuth.currentUser?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .build()
            )?.await()
            firebaseFireStore.collection("users")
                .document(firebaseAuth.currentUser?.uid.toString())
                .set(
                    User(
                        email = email,
                        name = userName,
                        id = firebaseAuth.currentUser?.uid.toString(),
                        password = password,
                        photoUrl = null
                    )
                ).await()
            true
        } catch (e: Exception) {
            throw Exception("Error sign up: ${e.message}")
        }
    }

    override suspend fun signupSeller(email: String, password: String, userName: String): Boolean {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            firebaseAuth.currentUser?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .build()
            )?.await()
            firebaseFireStore.collection("sellers")
                .document(firebaseAuth.currentUser?.uid.toString())
                .set(
                    Seller(
                        email = email,
                        name = userName,
                        id = firebaseAuth.currentUser?.uid.toString(),
                        password = password,
                        photoUrl = null,
                        description = "",
                        location = "",
                        contactInfo = ""
                    )
                ).await()
            true
        } catch (e: Exception) {
            throw Exception("Error sign up: ${e.message}")
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}