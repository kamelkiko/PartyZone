package com.app.partyzone.ui.screen.favourite

import javax.annotation.concurrent.Immutable

@Immutable
data class FavouriteState(
    val favouriteState: List<FavouriteItemState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

@Immutable
data class FavouriteItemState(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val imageUrl: String? = null,
    val price: Double? = null,
    val userId: String = "",
    val itemId: String = "",
    val type: String = "",
)