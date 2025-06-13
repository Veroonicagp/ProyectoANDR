/**
 * @file NotificationWorker.kt
 * @brief Worker para el manejo de notificaciones en background
 * @details Gestiona la creación y muestra de notificaciones usando WorkManager
 * @author ReadyToEnjoy Team
 * @version 1.0
 */

package com.example.readytoenjoy.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.readytoenjoy.R

/**
 * @class NotificationWorker
 * @brief Worker que maneja la creación de notificaciones en background
 * @details Extiende Worker para ejecutar tareas de notificación de forma asíncrona.
 *          Crea canales de notificación compatibles con Android O+ y muestra notificaciones
 *          con títulos y mensajes personalizables.
 */
class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    companion object {
        /** @brief ID del canal de notificaciones para actividades */
        private const val CHANNEL_ID = "activity_created_channel"

        /** @brief ID único para las notificaciones */
        private const val NOTIFICATION_ID = 1001

        /** @brief Clave para el título en los datos de entrada */
        const val KEY_TITLE = "title"

        /** @brief Clave para el mensaje en los datos de entrada */
        const val KEY_MESSAGE = "message"
    }

    /**
     * @brief Ejecuta el trabajo de crear y mostrar la notificación
     * @details Obtiene los datos de entrada, crea el canal de notificación si es necesario,
     *          construye la notificación y la muestra al usuario
     * @return Result.success() si la operación fue exitosa
     */
    override fun doWork(): Result {
        val title = inputData.getString(KEY_TITLE) ?: "Actividad creada"
        val message = inputData.getString(KEY_MESSAGE) ?: "Tu actividad fue creada con éxito."

        createNotificationChannel()
        showNotification(title, message)

        return Result.success()
    }

    /**
     * @brief Crea el canal de notificación para Android O+
     * @details Configura un canal de notificación con importancia alta para dispositivos
     *          con Android API 26 o superior
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = applicationContext
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Notificaciones de actividad",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones cuando se crean o modifican actividades"
                enableVibration(true)
                enableLights(true)
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * @brief Construye y muestra la notificación
     * @details Crea una notificación con el título y mensaje especificados,
     *          la configura para que se cancele automáticamente al tocarla
     * @param title Título de la notificación
     * @param message Mensaje del cuerpo de la notificación
     */
    private fun showNotification(title: String, message: String) {
        val notificationManager = applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_save)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
