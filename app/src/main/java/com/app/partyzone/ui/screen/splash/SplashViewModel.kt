package com.app.partyzone.ui.screen.splash

import androidx.lifecycle.ViewModel
import com.app.partyzone.core.domain.repository.AuthRepository
import com.app.partyzone.core.domain.repository.IUserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPreferencesRepository: IUserPreferencesRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    suspend fun isFirstTimeOpenApp(): Boolean {
        return userPreferencesRepository.getUserIsFirstTimeOpenApp()
    }

    suspend fun isUserLoggedIn(): Boolean {
        return authRepository.isLoggedIn()
    }
}