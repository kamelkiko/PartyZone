package com.app.partyzone.core.domain.repository

interface AuthRepository {
    suspend fun loginUser(email: String, password: String): Boolean
    suspend fun loginSeller(email: String, password: String): Boolean
    suspend fun signupUser(email: String, password: String, userName: String): Boolean
    suspend fun signupSeller(email: String, password: String, userName: String): Boolean
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}