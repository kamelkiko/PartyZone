package com.app.partyzone.core.domain.repository

import com.app.partyzone.core.domain.entity.Notification
import com.app.partyzone.core.domain.entity.Request
import com.app.partyzone.core.domain.entity.Seller
import kotlinx.coroutines.flow.Flow

interface SellerRepository {
    suspend fun getCurrentSeller(): Seller
    suspend fun getSellerById(id: String): Seller
    suspend fun updateCurrentSeller(seller: Seller)
    suspend fun getNotifications(): List<Notification>
    suspend fun sendNotification(notification: Notification)
    fun hasNotification(): Flow<Boolean>
    suspend fun acceptRequest(requestId: String)
    suspend fun cancelRequest(requestId: String)
    suspend fun fetchSellerRequests(): List<Request>
}