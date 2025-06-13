package com.example.readytoenjoy.core.data.repository.user

import com.example.readytoenjoy.core.data.local.user.UserLocal
import com.example.readytoenjoy.core.data.network.ReadyToEnjoyApiService
import com.example.readytoenjoy.core.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userLocal: UserLocal,
    private val api: ReadyToEnjoyApiService
) : UserRepositoryInterface {

    override suspend fun getCurrentUser(): User? {
        return userLocal.getUser()
    }

    override suspend fun getUserById(userId: String): User? {
        return null
    }

    override suspend fun getUserByAdvenId(advenId: String): User? {
        return null
    }

    override suspend fun deleteUser(userId: String): Result<Unit> {
        return try {
            val response = api.deleteUser(userId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}