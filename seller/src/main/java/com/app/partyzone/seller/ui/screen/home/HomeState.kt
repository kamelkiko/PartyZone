package com.app.partyzone.seller.ui.screen.home

import javax.annotation.concurrent.Immutable

@Immutable
data class HomeState(
    val sellerState: SellerState = SellerState(),
    val isLoading: Boolean = false,
    val hasNotifications: Boolean = false,
    val error: String? = null,
)

@Immutable
data class SellerState(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val description: String = "",
    val location: String = "",
    val contactInfo: String = "",
)