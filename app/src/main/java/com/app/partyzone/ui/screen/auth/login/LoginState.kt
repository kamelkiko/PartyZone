package com.app.partyzone.ui.screen.auth.login

import javax.annotation.concurrent.Immutable

@Immutable
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)