package com.app.partyzone.core.domain.entity

data class User(
    val id: String,
    val name: String,
    val email: String,
    val photoUrl: String?,
    val favouriteSellers: List<String> = emptyList(),
)