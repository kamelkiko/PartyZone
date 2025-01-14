package com.app.partyzone.core.domain.repository

import com.app.partyzone.core.domain.entity.Notification
import com.app.partyzone.core.domain.entity.Seller

interface SellerRepository {
    suspend fun getCurrentSeller(): Seller
    suspend fun getSellerById(id: String): Seller
    suspend fun updateCurrentSeller(seller: Seller)
    suspend fun getNotifications(): List<Notification>
    suspend fun sendNotification(notification: Notification)
    suspend fun hasNotification(): Boolean
}