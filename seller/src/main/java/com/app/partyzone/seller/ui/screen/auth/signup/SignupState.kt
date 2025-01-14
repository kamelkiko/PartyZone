package com.app.partyzone.seller.ui.screen.auth.signup

import javax.annotation.concurrent.Immutable

@Immutable
data class SignupState(
    val email: String = "",
    val password: String = "",
    val userName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)