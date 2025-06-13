/**
 * @file UserRepositoryInterface.kt
 * @brief Interface para el repositorio de usuarios
 * @details Define las operaciones disponibles para la gestión de usuarios en la aplicación
 * @author ReadyToEnjoy Team
 * @version 1.0
 */

package com.example.readytoenjoy.core.data.repository.user

import com.example.readytoenjoy.core.model.User

/**
 * @interface UserRepositoryInterface
 * @brief Interface que define las operaciones del repositorio de usuarios
 * @details Proporciona métodos para obtener, eliminar y gestionar usuarios del sistema
 */
interface UserRepositoryInterface {

    /**
     * @brief Obtiene el usuario actualmente autenticado
     * @details Recupera la información del usuario que tiene sesión activa
     * @return User? El usuario actual o null si no hay sesión activa
     */
    suspend fun getCurrentUser(): User?

    /**
     * @brief Elimina un usuario del sistema
     * @details Elimina completamente un usuario basado en su ID
     * @param userId ID único del usuario a eliminar
     * @return Result<Unit> Resultado de la operación (exitosa o con error)
     */
    suspend fun deleteUser(userId: String): Result<Unit>
}