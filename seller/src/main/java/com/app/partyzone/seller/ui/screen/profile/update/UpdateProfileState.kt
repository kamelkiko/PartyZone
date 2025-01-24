package com.app.partyzone.seller.ui.screen.profile.update

import javax.annotation.concurrent.Immutable

@Immutable
data class UpdateProfileState(
    val isLoading: Boolean = false,
    val isLoadingGetUser: Boolean = false,
    val name: String = "",
    val email: String = "",
    val oldPassword: String = "",
    val newPassword: String = "",
    val imageUri: String = "",
)