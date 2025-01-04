package com.app.partyzone.ui.screen.google

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.app.partyzone.domain.repository.AuthGoogleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GoogleAuthViewModel @Inject constructor(
    private val googleSignInUserUseCase: AuthGoogleRepository,
    private val googleSignInWIthIntentUserUseCase: AuthGoogleRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(SignInUiState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: Result<Boolean>) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.isSuccess,
                signInError = result.exceptionOrNull()?.message
            )
        }
    }

    suspend fun signIn() = googleSignInUserUseCase.signIn()

    fun resetState() {
        _state.update { SignInUiState() }
    }

    suspend fun signInWithIntent(intent: Intent): Result<Boolean> {
        return googleSignInWIthIntentUserUseCase.signInWithIntent(intent)
    }
}