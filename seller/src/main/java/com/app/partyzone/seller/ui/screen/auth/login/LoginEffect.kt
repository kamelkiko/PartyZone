package com.app.partyzone.seller.ui.screen.auth.login

sealed interface LoginEffect {
    data class ShowToast(val message: String) : LoginEffect
    data object NavigateToHome : LoginEffect
    data object NavigateToSignup : LoginEffect
}