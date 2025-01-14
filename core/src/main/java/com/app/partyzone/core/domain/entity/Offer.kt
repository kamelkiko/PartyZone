package com.app.partyzone.core.domain.entity

data class Offer(
    val id: String = "",
    val sellerId: String = "",
    val sellerName: String = "",
    val sellerImageUrl: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val category: String = "",
    val reviews: List<Review> = emptyList()
)
