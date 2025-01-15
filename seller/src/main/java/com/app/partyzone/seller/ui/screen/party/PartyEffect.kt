package com.app.partyzone.seller.ui.screen.party

sealed interface PartyEffect {
    data class NavigateToDetails(val id: String, val type: String) : PartyEffect
}