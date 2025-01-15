package com.app.partyzone.seller.ui.screen.party

import com.app.partyzone.core.domain.repository.UserRepository
import com.app.partyzone.seller.ui.base.BaseViewModel
import com.app.partyzone.seller.ui.base.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PartyViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel<PartyState, PartyEffect>(PartyState()) {

    private fun fetchFavourites() {
        updateState { it.copy(isLoading = true, error = null, partyState = emptyList()) }
        tryToExecute(
            function = { userRepository.getFavorites() },
            onSuccess = { favourite ->
                updateState {
                    it.copy(
                        isLoading = false,
                        partyState = favourite.map { item ->
                            PartyItemState(
                                id = item.id,
                                name = item.name,
                                location = item.location,
                                imageUrl = item.imageUrl,
                                price = item.price,
                                userId = item.userId,
                                itemId = item.itemId,
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

    fun onClickFavouriteItem(id: String, type: String) {
        sendNewEffect(PartyEffect.NavigateToDetails(id, type))
    }
}