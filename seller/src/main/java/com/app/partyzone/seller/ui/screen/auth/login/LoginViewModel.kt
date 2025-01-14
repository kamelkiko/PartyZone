package com.app.partyzone.seller.ui.screen.auth.login

import com.app.partyzone.core.domain.repository.AuthRepository
import com.app.partyzone.core.util.isEmptyOrBlank
import com.app.partyzone.seller.ui.base.BaseViewModel
import com.app.partyzone.seller.ui.base.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseViewModel<LoginState, LoginEffect>(LoginState()) {

    fun onEmailChanged(email: String) {
        updateState {
            it.copy(email = email)
        }
    }

    fun onPasswordChanged(password: String) {
        updateState {
            it.copy(password = password)
        }
    }

    fun onSignUp() {
        sendNewEffect(LoginEffect.NavigateToSignup)
    }

    fun onLogin() {
        updateState {
            it.copy(isLoading = true, error = null)
        }
        if (state.value.email.isEmptyOrBlank()) {
            updateState {
                it.copy(
                    isLoading = false,
                    error = "Please enter your email"
                )
            }
            return
        }
        if (state.value.password.isEmptyOrBlank()) {
            updateState {
                it.copy(
                    isLoading = false,
                    error = "Please enter your password"
                )
            }
            return
        }
        tryToExecute(
            function = {
                authRepository.loginSeller(
                    email = state.value.email,
                    password = state.value.password
                )
            },
            onSuccess = {
                updateState { it.copy(isLoading = false, error = null) }
                sendNewEffect(LoginEffect.NavigateToHome)
            },
            onError = ::handleError
        )
    }

    private fun handleError(error: ErrorState) {
        when (error) {
            is ErrorState.NetworkError -> {
                updateState {
                    it.copy(
                        isLoading = false,
                        error = "No internet connection"
                    )
                }
            }

            is ErrorState.UnknownError -> {
                updateState {
                    it.copy(
                        isLoading = false,
                        error = error.message.toString()
                    )
                }
            }

            is ErrorState.WrongPassword -> {
                updateState {
                    it.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            }

            is ErrorState.UserNotFound -> {
                updateState {
                    it.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            }

            else -> {
                updateState {
                    it.copy(
                        isLoading = false,
                        error = "Something happen please try again later!"
                    )
                }
            }
        }
    }
}