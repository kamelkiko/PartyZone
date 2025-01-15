package com.app.partyzone.core.domain.entity

import com.google.firebase.Timestamp

data class Post(
    val id: String = "",
    val sellerId: String = "",
    val sellerName: String = "",
    val sellerImageUrl: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val category: String = "",
    val reviews: List<Review> = emptyList(),
    val createdAt: Timestamp = Timestamp.now()
)