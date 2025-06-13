/**
 * @file UserRepository.kt
 * @brief Implementación del repositorio de usuarios
 * @details Implementa las operaciones de gestión de usuarios usando fuentes locales y remotas
 * @author ReadyToEnjoy Team
 * @version 1.0
 */

package com.example.readytoenjoy.core.data.repository.user

import com.example.readytoenjoy.core.data.local.user.UserLocal
import com.example.readytoenjoy.core.data.network.ReadyToEnjoyApiService
import com.example.readytoenjoy.core.model.User
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @class UserRepository
 * @brief Repositorio principal para la gestión de usuarios
 * @details Combina fuentes de datos locales y remotas para operaciones de usuario
 */
@Singleton
class UserRepository @Inject constructor(
    private val userLocal: UserLocal,
    private val api: ReadyToEnjoyApiService
) : UserRepositoryInterface {

    /**
     * @brief Obtiene el usuario actualmente autenticado desde el almacenamiento local
     * @details Recupera la información del usuario almacenada localmente
     * @return User? El usuario actual o null si no existe
     */
    override suspend fun getCurrentUser(): User? {
        return userLocal.getUser()
    }

    /**
     * @brief Elimina un usuario del sistema remoto
     * @details Envía una petición al servidor para eliminar el usuario
     * @param userId ID único del usuario a eliminar
     * @return Result<Unit> Resultado de la operación
     * @throws Exception Si hay problemas de conectividad o el servidor responde con error
     */
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