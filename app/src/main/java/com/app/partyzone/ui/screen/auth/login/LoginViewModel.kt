package com.app.partyzone.ui.screen.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.partyzone.domain.repository.AuthGoogleRepository
import com.app.partyzone.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authGoogleRepository: AuthGoogleRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state get() = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect get() = _effect.asSharedFlow()

    fun onEmailChanged(email: String) {
        _state.update {
            it.copy(email = email)
        }
    }

    fun onPasswordChanged(password: String) {
        _state.update {
            it.copy(password = password)
        }
    }

    fun onSignUp() {
        viewModelScope.launch(Dispatchers.IO) {
            _effect.emit(LoginEffect.NavigateToSignup)
        }
    }

    fun onLogin() {
        _state.update {
            it.copy(isLoading = true, error = null)
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val isSuccess =
                    authRepository.login(email = state.value.email, password = state.value.password)
                if (isSuccess) {
                    _state.update {
                        it.copy(isLoading = false, error = null)
                    }
                    _effect.emit(LoginEffect.NavigateToHome)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false, error = e.message.toString())
                }
                _effect.emit(LoginEffect.ShowToast(e.message.toString()))
            }
        }
    }
}