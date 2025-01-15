package com.app.partyzone.seller.ui.screen.party

import com.app.partyzone.core.domain.entity.Status
import com.google.firebase.Timestamp
import javax.annotation.concurrent.Immutable

@Immutable
data class PartyState(
    val selectedType: Status = Status.Upcoming,
    val partyState: List<PartyItemState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

@Immutable
data class PartyItemState(
    val id: String = "",
    val userId: String = "", // User who sent the request
    val sellerId: String = "", // Seller who received the request
    val itemId: String = "", // ID of the post or offer
    val itemName: String = "", // Name of the post or offer
    val itemImageUrl: String = "", // Image URL of the post or offer
    val type: String = "", // "post" or "offer"
    val status: String = "Pending", // Pending, Completed, Cancelled
    val createdAt: Timestamp = Timestamp.now(), // Timestamp of when the request was create
)