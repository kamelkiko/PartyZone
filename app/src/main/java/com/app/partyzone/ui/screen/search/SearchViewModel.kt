package com.app.partyzone.ui.screen.search

import com.app.partyzone.core.domain.entity.Favorite
import com.app.partyzone.core.domain.repository.UserRepository
import com.app.partyzone.ui.base.BaseViewModel
import com.app.partyzone.ui.base.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel<SearchState, SearchEffect>(SearchState()) {

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

    }

    fun onClickSearchItem(id: String, type: String) {
        sendNewEffect(SearchEffect.NavigateToDetails(id, type))
    }

    fun onClickFavIcon(id: String) {
        updateState { it.copy(isLoading = true, error = null) }
        val item = state.value.searchState.find { it.id == id }
        tryToExecute(
            function = {
                if (item?.isFavourite == true)
                    userRepository.removeFromFavorites(item.favId ?: "")
                else if (item?.isFavourite == false)
                    userRepository.addToFavorites(
                        Favorite(
                            id = UUID.randomUUID().toString(),
                            userId = "",
                            sellerId = item.sellerId ?: "",
                            itemId = item.id,
                            name = item.name,
                            location = item.location,
                            imageUrl = item.imageUrl,
                            price = item.price,
                            type = item.type
                        )
                    )
            },
            onSuccess = {
                updateState { it.copy(isLoading = false, error = null) }
                onQueryChange(query = state.value.query)
            },
            onError = ::handleError
        )
    }

    fun onQueryChange(query: String) {
        updateState { it.copy(query = query, isLoading = true, error = null) }
        tryToExecute(
            function = { userRepository.searchAll(query) },
            onSuccess = { result ->
                updateState {
                    it.copy(
                        isLoading = false,
                        error = null,
                        searchState = result.map { searchItem ->
                            SearchItemState(
                                id = searchItem.id,
                                name = searchItem.name,
                                location = searchItem.location,
                                imageUrl = searchItem.imageUrl,
                                type = searchItem.type,
                                isFavourite = searchItem.isFav,
                                sellerId = searchItem.sellerId,
                                favId = searchItem.favId,
                                price = searchItem.price,
                            )
                        }
                    )
                }
            },
            onError = ::handleError
        )
    }
}