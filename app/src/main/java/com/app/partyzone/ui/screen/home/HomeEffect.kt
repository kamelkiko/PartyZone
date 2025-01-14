package com.app.partyzone.ui.screen.home

sealed interface HomeEffect {
    data object NavigateToSearch : HomeEffect
    data object NavigateToNotification : HomeEffect
}