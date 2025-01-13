package com.app.partyzone.ui.screen.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.partyzone.core.domain.repository.AuthRepository
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
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SignupState())
    val state get() = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SignupEffect>()
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

    fun onUserNameChanged(userName: String) {
        _state.update {
            it.copy(userName = userName)
        }
    }

    fun onSignIn() {
        viewModelScope.launch(Dispatchers.IO) {
            _effect.emit(SignupEffect.NavigateToLogin)
        }
    }

    fun onSingUp() {
        _state.update {
            it.copy(isLoading = true, error = null)
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val isSuccess =
                    authRepository.signup(
                        email = state.value.email,
                        password = state.value.password,
                        userName = state.value.userName
                    )
                if (isSuccess) {
                    _state.update {
                        it.copy(isLoading = false, error = null)
                    }
                    _effect.emit(SignupEffect.NavigateToHome)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false, error = e.message.toString())
                }
                _effect.emit(SignupEffect.ShowToast(e.message.toString()))
            }
        }
    }
}