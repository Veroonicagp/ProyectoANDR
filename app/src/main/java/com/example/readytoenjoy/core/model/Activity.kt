
/**
 * @file Activity.kt
 * @brief Modelo de datos para actividades
 * @details Define la estructura de datos para una actividad en la aplicación
 * @author ReadyToEnjoy Team
 * @version 1.0
 */

package com.example.readytoenjoy.core.model

import android.net.Uri

/**
 * @class Activity
 * @brief Representa una actividad turística en la aplicación
 * @details Contiene toda la información necesaria para describir una actividad,
 *          incluyendo detalles, ubicación, precio e imagen asociada
 *
 * @property id Identificador único de la actividad
 * @property title Título descriptivo de la actividad
 * @property location Ubicación donde se realiza la actividad
 * @property price Precio de la actividad en formato string
 * @property description Descripción detallada de la actividad
 * @property advenId ID del aventurero que creó la actividad (puede ser null)
 * @property img URI de la imagen asociada a la actividad (opcional)
 */
data class Activity(
    val id: String,
    val title: String,
    val location: String,
    val price: String,
    val description: String,
    val advenId: String?,
    val img: Uri?,
)