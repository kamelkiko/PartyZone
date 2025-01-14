package com.app.partyzone.seller.ui.screen.home

sealed interface HomeEffect {
    data object NavigateToNotification : HomeEffect
}