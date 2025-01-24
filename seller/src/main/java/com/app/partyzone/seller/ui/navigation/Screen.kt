package com.app.partyzone.seller.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Splash : Screen

    @Serializable
    data object Login : Screen

    @Serializable
    data object Signup : Screen

    @Serializable
    data object Onboarding : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data object Party : Screen

    @Serializable
    data object Profile : Screen

    @Serializable
    data object UpdateProfile : Screen

    @Serializable
    data object Setting : Screen

    @Serializable
    data object Notification : Screen
}