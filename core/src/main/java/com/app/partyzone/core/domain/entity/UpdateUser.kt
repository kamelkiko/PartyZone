package com.app.partyzone.core.domain.entity

import android.net.Uri

data class UpdateUser(
    val name: String,
    val email: String,
    val oldPassword: String,
    val newPassword: String,
    val photoUrl: String? = null,
    val imageUri: Uri? = null,
)