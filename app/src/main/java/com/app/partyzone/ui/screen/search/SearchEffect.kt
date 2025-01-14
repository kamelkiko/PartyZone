package com.app.partyzone.ui.screen.search

sealed interface SearchEffect {
    data class NavigateToDetails(val id: String, val type: String) : SearchEffect
}