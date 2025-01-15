package com.app.partyzone.ui.screen.party

sealed interface PartyEffect {
    data class NavigateToDetails(val id: String, val type: String) : PartyEffect
}