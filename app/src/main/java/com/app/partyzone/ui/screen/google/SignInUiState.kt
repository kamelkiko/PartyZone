package com.app.partyzone.ui.screen.google

import android.content.IntentSender

data class SignInUiState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
    val intentSender: IntentSender?=null,
)