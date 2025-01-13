package com.app.partyzone.core.domain.repository

import android.content.Intent
import android.content.IntentSender

interface AuthGoogleRepository {
    suspend fun signIn(): IntentSender?
    suspend fun signInWithIntent(intent: Intent): Result<Boolean>
    suspend fun signOut()
//    suspend fun getSignedInUser(): Any?
//    suspend fun getUserById(userId: String): Any?
//    suspend fun checkIfUserIsLoggedIn(): Boolean
//    suspend fun updateUserStatue(status: String)
//    suspend fun getOnlineFriends(): CollectionReference
}