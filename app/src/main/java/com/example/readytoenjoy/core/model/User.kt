/**
 * @file User.kt
 * @brief Modelo de datos para usuarios del sistema
 * @details Define la estructura de datos para la autenticación y autorización
 * @author ReadyToEnjoy Team
 * @version 1.0
 */

package com.example.readytoenjoy.core.model

/**
 * @class User
 * @brief Representa un usuario autenticado en el sistema
 * @details Contiene información de autenticación y autorización del usuario
 *
 * @property id Identificador único del usuario
 * @property name Nombre del usuario
 * @property email Dirección de correo electrónico
 * @property advenId ID del aventurero asociado a este usuario
 * @property token Token JWT para autenticación (puede ser null)
 * @property isAdmin Indica si el usuario tiene privilegios de administrador
 */
data class User(
    val id: String,
    val name: String,
    val email: String,
    val advenId: String,
    val token: String?,
    val isAdmin: Boolean = false
)