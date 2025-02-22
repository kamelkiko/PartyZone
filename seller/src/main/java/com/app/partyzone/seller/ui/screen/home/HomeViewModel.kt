package com.app.partyzone.seller.ui.screen.home

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.app.partyzone.core.domain.SellerPost
import com.app.partyzone.core.domain.repository.PostRepository
import com.app.partyzone.core.domain.repository.SellerRepository
import com.app.partyzone.seller.ui.base.BaseViewModel
import com.app.partyzone.seller.ui.base.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sellerRepository: SellerRepository,
    private val postRepository: PostRepository,
) : BaseViewModel<HomeState, HomeEffect>(HomeState()) {
    init {
        fetchUser()
        hasNotifications()
    }

    val posts: StateFlow<List<SellerPost>> = postRepository.getSellerPosts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun createPost(post: SellerPost, images: List<Uri>) {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            function = { postRepository.createPost(post, images) },
            onSuccess = { updateState { it.copy(isLoading = false) } },
            onError = ::handleError
        )
    }

    fun hasNotifications() {
        updateState { it.copy(hasNotifications = false) }
        tryToCollect(
            function = { sellerRepository.hasNotification() },
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
        updateState { it.copy(isLoading = true, error = null, sellerState = SellerState()) }
        tryToExecute(
            function = { sellerRepository.getCurrentSeller() },
            onSuccess = { seller ->
                updateState {
                    it.copy(
                        isLoading = false,
                        sellerState = SellerState(
                            id = seller.id,
                            name = seller.name,
                            email = seller.email,
                            photoUrl = seller.photoUrl,
                            location = seller.location,
                            contactInfo = seller.contactInfo,
                            description = seller.description
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

    fun onClickRetry() {
        fetchUser()
    }

    fun onClickNotification() {
        sendNewEffect(HomeEffect.NavigateToNotification)
    }
}