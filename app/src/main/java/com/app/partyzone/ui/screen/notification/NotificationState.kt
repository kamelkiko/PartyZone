package com.app.partyzone.ui.screen.notification

import javax.annotation.concurrent.Immutable

@Immutable
data class NotificationState(
    val favouriteState: List<NotificationItemState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

@Immutable
data class NotificationItemState(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val imageUrl: String? = null,
    val price: Double? = null,
    val userId: String = "",
    val itemId: String = "",
    val type: String = "",
    val sellerId: String = "",
)