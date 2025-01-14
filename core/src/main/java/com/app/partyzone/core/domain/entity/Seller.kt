package com.app.partyzone.core.domain.entity

data class Seller(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val description: String,
    val location: String,
    val photoUrl: String?,
    val contactInfo: String
)