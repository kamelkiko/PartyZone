package com.app.partyzone.ui.screen.auth.signup

import javax.annotation.concurrent.Immutable

@Immutable
data class SignupState(
    val email: String = "",
    val password: String = "",
    val userName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)