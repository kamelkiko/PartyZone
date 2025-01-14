package com.app.partyzone.seller.ui.screen.notification

import javax.annotation.concurrent.Immutable

@Immutable
data class NotificationState(
    val notificationState: List<NotificationItemState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

@Immutable
data class NotificationItemState(
    val id: String = "",
    val message: String = "",
    val isRead: Boolean = false,
    val date: String = "",
    val userId: String = "",
    val type: String = "",
    val sellerId: String = "",
)