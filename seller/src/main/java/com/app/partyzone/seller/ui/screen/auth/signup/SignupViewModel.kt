package com.app.partyzone.seller.ui.screen.auth.signup

import com.app.partyzone.core.domain.repository.AuthRepository
import com.app.partyzone.core.util.isEmptyOrBlank
import com.app.partyzone.seller.ui.base.BaseViewModel
import com.app.partyzone.seller.ui.base.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel<SignupState, SignupEffect>(SignupState()) {

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

    fun onUserNameChanged(userName: String) {
        updateState {
            it.copy(userName = userName)
        }
    }

    fun onLogIn() {
        sendNewEffect(SignupEffect.NavigateToLogin)
    }

    fun onSingUp() {
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
        if (state.value.userName.isEmptyOrBlank()) {
            updateState {
                it.copy(
                    isLoading = false,
                    error = "Please enter your name"
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
                authRepository.signupSeller(
                    email = state.value.email.trim(),
                    password = state.value.password.trim(),
                    userName = state.value.userName
                )
            },
            onSuccess = {
                updateState { it.copy(isLoading = false, error = null) }
                sendNewEffect(SignupEffect.NavigateToHome)
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

            is ErrorState.UserAlreadyExists -> {
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