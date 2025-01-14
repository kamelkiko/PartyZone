package com.app.partyzone.ui.screen.home

import com.app.partyzone.core.domain.repository.UserRepository
import com.app.partyzone.ui.base.BaseViewModel
import com.app.partyzone.ui.base.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel<HomeState, HomeEffect>(HomeState()) {
    init {
        fetchUser()
        hasNotifications()
    }

    fun hasNotifications() {
        updateState { it.copy(hasNotifications = false) }
        tryToExecute(
            function = { userRepository.hasNotification() },
            onSuccess = { hasNotification ->
                updateState {
                    it.copy(
                        hasNotifications = hasNotification
                    )
                }
            },
            onError = {}
        )
    }

    private fun fetchUser() {
        updateState { it.copy(isLoading = true, error = null, userState = UserState()) }
        tryToExecute(
            function = { userRepository.getCurrentUser() },
            onSuccess = { user ->
                updateState {
                    it.copy(
                        isLoading = false,
                        userState = UserState(
                            id = user.id,
                            name = user.name,
                            email = user.email,
                            photoUrl = user.photoUrl,
                        ),
                        error = null
                    )
                }
            },
            onError = { error ->
                handleError(error)
            }
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

            is ErrorState.NotFound -> {
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

    fun onClickRetry() {
        fetchUser()
    }

    fun onClickNotification() {
        sendNewEffect(HomeEffect.NavigateToNotification)
    }

    fun onClickSearch() {
        sendNewEffect(HomeEffect.NavigateToSearch)
    }
}