package com.app.partyzone.core.domain.repository

interface IUserPreferencesRepository {
    suspend fun getUserIsLoggedIn(): Boolean
    suspend fun setUserLoggedIn()
    suspend fun setUserLoggedOut()
}