package com.app.partyzone.ui.screen.favourite

sealed interface FavouriteEffect {
    data class NavigateToDetails(val id: String, val type: String) : FavouriteEffect
}