package com.app.partyzone.core.domain

import com.google.firebase.Timestamp

data class SellerPost(
    val id: String = "",
    val sellerId: String = "",
    val sellerImageUrl: String = "",
    val sellerName: String = "",
    val description: String = "",
    val category: String = "",
    val images: List<String> = emptyList(),
    val timestamp: Timestamp = Timestamp.now(),
    val likes: Int = 0,
    val reviews: List<Comment> = emptyList(),
)

data class Comment(
    val userId: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now(),
)