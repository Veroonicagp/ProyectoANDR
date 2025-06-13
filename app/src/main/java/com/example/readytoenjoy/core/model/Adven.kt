/**
 * @file Adven.kt
 * @brief Modelo de datos para aventureros
 * @details Define la estructura de datos para un aventurero (usuario) en la aplicación
 * @author ReadyToEnjoy Team
 * @version 1.0
 */

package com.example.readytoenjoy.core.model

import android.net.Uri

/**
 * @class Adven
 * @brief Representa un aventurero (usuario) en la aplicación
 * @details Contiene la información del perfil de un aventurero que puede crear actividades
 *
 * @property id Identificador único del aventurero
 * @property name Nombre del aventurero
 * @property email Dirección de correo electrónico
 * @property media URI de la foto de perfil (opcional)
 */
data class Adven(
    val id: String,
    val name: String,
    val email: String,
    val media: Uri?
)