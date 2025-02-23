package com.app.partyzone.core.domain.entity

import android.net.Uri

data class UpdateSeller(
    val name: String,
    val email: String,
    val oldPassword: String,
    val newPassword: String,
    val photoUrl: String? = null,
    val phoneNumber: String = "",
    val imageUri: Uri? = null,
)