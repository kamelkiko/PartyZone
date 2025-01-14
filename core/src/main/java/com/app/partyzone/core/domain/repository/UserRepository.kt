package com.app.partyzone.core.domain.repository

import com.app.partyzone.core.domain.entity.Favorite
import com.app.partyzone.core.domain.entity.Notification
import com.app.partyzone.core.domain.entity.Request
import com.app.partyzone.core.domain.entity.Seller
import com.app.partyzone.core.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getCurrentUser(): User
    suspend fun getUserById(id: String): User
    suspend fun updateCurrentUser(user: User)
    suspend fun addToFavorites(favorite: Favorite)
    suspend fun removeFromFavorites(favoriteId: String)
    suspend fun getFavorites(): List<Favorite>
    suspend fun getNotifications(): List<Notification>
    suspend fun sendNotification(notification: Notification)
    fun hasNotification(): Flow<Boolean>
    suspend fun searchSellers(query: String): List<Seller>
    suspend fun sendRequest(request: Request)
    suspend fun cancelRequest(requestId: String)
    suspend fun fetchUserRequests(userId: String): List<Request>
}