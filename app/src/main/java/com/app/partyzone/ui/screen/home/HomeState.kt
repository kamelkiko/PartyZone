package com.app.partyzone.ui.screen.home

import javax.annotation.concurrent.Immutable

@Immutable
data class HomeState(
    val userState: UserState = UserState(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasNotifications: Boolean = false,
)

@Immutable
data class UserState(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String? = null,
)