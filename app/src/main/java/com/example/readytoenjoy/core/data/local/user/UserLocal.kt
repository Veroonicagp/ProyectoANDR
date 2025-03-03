package com.example.readytoenjoy.core.data.local.user

import com.example.readytoenjoy.core.model.User

interface UserLocal {
    suspend fun saveUser(user: User)
    suspend fun retrieveUser(): User?
    suspend fun clearUser()
}