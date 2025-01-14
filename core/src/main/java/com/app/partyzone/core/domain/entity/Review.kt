package com.app.partyzone.core.domain.entity

data class Review(
    val userId: String = "",
    val userName: String = "",
    val userImageUrl: String = "",
    val rating: Float = 0f,
    val comment: String = ""
)