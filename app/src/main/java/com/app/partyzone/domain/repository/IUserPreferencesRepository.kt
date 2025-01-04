package com.app.partyzone.domain.repository

interface IUserPreferencesRepository {
    suspend fun getUserIsLoggedIn(): Boolean
    suspend fun setUserLoggedIn()
    suspend fun setUserLoggedOut()
}