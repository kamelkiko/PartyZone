package com.app.partyzone.core.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun signup(email: String, password: String, userName: String): Boolean
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}