package com.app.partyzone.core.domain.entity

import com.google.firebase.Timestamp

data class Request(
    val id: String = "",
    val userId: String = "", // User who sent the request
    val sellerId: String = "", // Seller who received the request
    val itemId: String = "", // ID of the post or offer
    val itemName: String = "", // Name of the post or offer
    val itemImageUrl: String = "", // Image URL of the post or offer
    val type: String = "", // "post" or "offer"
    val status: String = "Pending", // Pending, Completed, Cancelled
    val createdAt: Timestamp = Timestamp.now() // Timestamp of when the request was create
)