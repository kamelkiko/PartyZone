package com.app.partyzone.ui.screen.search

import javax.annotation.concurrent.Immutable

@Immutable
data class SearchState(
    val searchState: List<SearchItemState> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)

@Immutable
data class SearchItemState(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val imageUrl: String? = null,
    val price: Double? = null,
    val sellerId: String? = null,
    val favId: String? = null,
    val type: String = "",
    val isFavourite: Boolean = false,
)