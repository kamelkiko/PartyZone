package com.app.partyzone.core.data.repository.remote

import android.content.Intent
import android.content.IntentSender
import com.app.partyzone.core.domain.repository.AuthGoogleRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthGoogleRepoImpl @Inject constructor(
    private val webClientId: String,
    private val oneTapClient: SignInClient,
    private val auth: FirebaseAuth,
) : AuthGoogleRepository {

    override suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            throw e
        }
        return result?.pendingIntent?.intentSender
    }

    override suspend fun signInWithIntent(intent: Intent): Result<Boolean> {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            auth.signInWithCredential(googleCredentials).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure<Exception>(e.cause ?: e)
            throw e
        }
    }

    override suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            throw e
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(webClientId)
                    .build()
            ).setAutoSelectEnabled(true)
            .build()
    }

    //override suspend fun getSignedInUser(): UserEntity? = getProfileById(auth.uid.toString())

//    private suspend fun getProfileById(userId: String): UserEntity? {
//        return try {
//            databaseFireStore.collection(USERS)
//                .document(userId)
//                .get()
//                .await().toObject<UserDto>()?.toDomain()
//        } catch (e: Exception) {
//            null
//        }
//    }

//    override suspend fun getUserById(userId: String): UserEntity? {
//        return getProfileById(userId)
//    }

//    override suspend fun checkIfUserIsLoggedIn(): Boolean {
//        return getSignedInUser() != null
//    }

//    override suspend fun updateUserStatue(status: String) {
//        databaseFireStore.collection(USERS)
//            .document(auth.uid.toString())
//            .update(STATUS, status)
//            .await()
//    }

//    override suspend fun getOnlineFriends(): CollectionReference {
//        return databaseFireStore.collection(USERS)
//    }

//    companion object {
//        private const val USERS = "Users"
//        private const val STATUS = "status"
//        private const val INVITES = "invites"
//    }
}