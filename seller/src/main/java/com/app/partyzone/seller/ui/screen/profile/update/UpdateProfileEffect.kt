package com.app.partyzone.seller.ui.screen.profile.update

sealed interface UpdateProfileEffect {
    data object NavigateBack : UpdateProfileEffect
    data class ShowToast(val message: String?) : UpdateProfileEffect
}