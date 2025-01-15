package com.app.partyzone.ui.screen.notification

import com.app.partyzone.core.domain.repository.UserRepository
import com.app.partyzone.ui.base.BaseViewModel
import com.app.partyzone.ui.base.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel<NotificationState, Unit>(NotificationState()) {
    init {
        fetchFavourites()
    }

    private fun fetchFavourites() {
        updateState { it.copy(isLoading = true, error = null, notificationState = emptyList()) }
        tryToExecute(
            function = { userRepository.getNotifications() },
            onSuccess = { notification ->
                updateState {
                    it.copy(
                        isLoading = false,
                        notificationState = notification.map { item ->
                            NotificationItemState(
                                id = item.id,
                                message = item.message,
                                date = item.timeStamp.toDate().toString(),
                                isRead = item.isRead,
                                userId = item.userId,
                                sellerId = item.sellerId,
                                type = item.type
                            )
                        },
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
        fetchFavourites()
    }
}