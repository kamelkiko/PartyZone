package com.app.partyzone.core.domain.entity

import com.google.firebase.Timestamp

data class Request(
    val id: String = "",
    val userId: String = "", // User who sent the request
    val sellerId: String = "", // Seller who received the request
    val offerId: String? = null, // Optional: If the request is related to an offer
    val postId: String? = null, // Optional: If the request is related to a post
    val status: String = "Pending", // Pending, Completed, Cancelled
    val createdAt: Timestamp = Timestamp.now() // Timestamp of when the request was created
)