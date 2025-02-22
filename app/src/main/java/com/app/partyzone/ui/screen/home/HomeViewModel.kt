package com.app.partyzone.ui.screen.home

import androidx.lifecycle.viewModelScope
import com.app.partyzone.core.domain.SellerPost
import com.app.partyzone.core.domain.entity.Request
import com.app.partyzone.core.domain.repository.UserRepository
import com.app.partyzone.ui.base.BaseViewModel
import com.app.partyzone.ui.base.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel<HomeState, HomeEffect>(HomeState()) {
    init {
        fetchUser()
        hasNotifications()
    }

    val posts: StateFlow<List<SellerPost>> = userRepository.getAllPosts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun sendRequest(request: Request) {
        updateState { it.copy(isLoadingRequest = true, message = null) }
        tryToExecute(
            function = { userRepository.sendRequest(request) },
            onSuccess = {
                updateState {
                    it.copy(
                        isLoadingRequest = false,
                        message = "Sent successfully"
                    )
                }
            },
            onError = { error ->
                if (error is ErrorState.UnknownError) {
                    updateState {
                        it.copy(
                            isLoadingRequest = false,
                            message = error.message.toString()
                        )
                    }
                } else handleError(error)
            }
        )
    }

    fun hasNotifications() {
        updateState { it.copy(hasNotifications = false) }
        tryToCollect(
            function = { userRepository.hasNotification() },
            onNewValue = { hasNotification ->
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
                        isLoadingRequest = false,
                        error = "No internet connection",
                        message = null
                    )
                }
            }

            is ErrorState.UnknownError -> {
                updateState {
                    it.copy(
                        isLoading = false,
                        isLoadingRequest = false,
                        error = error.message.toString(),
                        message = null
                    )
                }
            }

            is ErrorState.NotFound -> {
                updateState {
                    it.copy(
                        isLoading = false,
                        isLoadingRequest = false,
                        error = error.message,
                        message = null
                    )
                }
            }

            else -> {
                updateState {
                    it.copy(
                        isLoading = false,
                        isLoadingRequest = false,
                        error = "Something happen please try again later!",
                        message = null
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