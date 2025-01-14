package com.app.partyzone.core.domain.repository

interface IUserPreferencesRepository {
    suspend fun getUserIsFirstTimeOpenApp(): Boolean
    suspend fun setUserFirstTimeUseApp()
}