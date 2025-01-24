package com.app.partyzone.core.domain.entity

data class UpdateSeller(
    val name: String,
    val email: String,
    val oldPassword: String,
    val newPassword: String,
    val photoUrl: String?
)