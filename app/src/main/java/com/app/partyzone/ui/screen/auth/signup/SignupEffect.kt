package com.app.partyzone.ui.screen.auth.signup

sealed interface SignupEffect {
    data class ShowToast(val message: String) : SignupEffect
    data object NavigateToHome : SignupEffect
    data object NavigateToLogin : SignupEffect
}