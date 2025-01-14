package com.app.partyzone.core.domain.repository

import com.app.partyzone.core.domain.entity.Favorite
import com.app.partyzone.core.domain.entity.Notification
import com.app.partyzone.core.domain.entity.User

interface UserRepository {
    suspend fun getCurrentUser(): User
    suspend fun getUserById(id: String): User
    suspend fun updateCurrentUser(user: User)
    suspend fun addToFavorites(favorite: Favorite)
    suspend fun removeFromFavorites(favoriteId: String)
    suspend fun getFavorites(): List<Favorite>
    suspend fun getNotifications(): List<Notification>
    suspend fun sendNotification(notification: Notification)
}