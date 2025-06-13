package com.example.readytoenjoy.core.data.repository.user

import com.example.readytoenjoy.core.model.User

interface UserRepositoryInterface {
    suspend fun getCurrentUser(): User?
    suspend fun getUserById(userId: String): User?
    suspend fun getUserByAdvenId(advenId: String): User?
    suspend fun deleteUser(userId: String): Result<Unit>
}