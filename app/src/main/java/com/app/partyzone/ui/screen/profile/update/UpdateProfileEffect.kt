package com.app.partyzone.ui.screen.profile.update

sealed interface UpdateProfileEffect {
    data object NavigateBack : UpdateProfileEffect
    data class ShowToast(val message: String?) : UpdateProfileEffect
}