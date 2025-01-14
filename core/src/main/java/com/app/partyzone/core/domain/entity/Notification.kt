package com.app.partyzone.core.domain.entity

import com.google.firebase.Timestamp

data class Notification(
    val id: String,
    val userId: String,
    val sellerId: String,
    val type: String, // "Request", "Accept", "Cancel", "Review", "Favorite", "Unfavorite", "ProfileView"
    val message: String,
    val date: String,
    val timeStamp: Timestamp = Timestamp.now(),
    val isRead: Boolean = false
)