package com.app.partyzone.ui.screen.party

import javax.annotation.concurrent.Immutable

@Immutable
data class PartyState(
    val partyState: List<PartyItemState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

@Immutable
data class PartyItemState(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val imageUrl: String? = null,
    val price: Double? = null,
    val userId: String = "",
    val itemId: String = "",
    val type: String = "",
    val sellerId: String = "",
    val status: String = "",
)