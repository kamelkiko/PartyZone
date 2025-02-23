package com.app.partyzone.seller.ui.screen.profile.update

import android.net.Uri
import javax.annotation.concurrent.Immutable

@Immutable
data class UpdateProfileState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoadingGetUser: Boolean = false,
    val name: String = "",
    val email: String = "",
    val oldPassword: String = "",
    val newPassword: String = "",
    val imageUri: Uri? = null,
    val photoUrl: String? = null,
    val phoneNumber: String = "",
)