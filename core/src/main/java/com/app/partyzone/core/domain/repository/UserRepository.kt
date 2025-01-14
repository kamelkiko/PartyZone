package com.app.partyzone.core.domain.repository

import com.app.partyzone.core.domain.entity.User

interface UserRepository {
    suspend fun getCurrentUser(): User
    suspend  fun getUserById(id: String): User
    suspend  fun updateCurrentUser(user: User)
}