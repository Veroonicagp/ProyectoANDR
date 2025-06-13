/**
 * @file DefaultActivityRepository.kt
 * @brief Implementación por defecto del repositorio de actividades
 * @details Gestiona la sincronización entre datos remotos y locales para actividades
 * @author ReadyToEnjoy Team
 * @version 1.0
 */

package com.example.readytoenjoy.core.data.repository.activity

import android.net.Uri
import android.util.Log
import com.example.readytoenjoy.core.data.local.activity.ActivityLocal
import com.example.readytoenjoy.core.data.network.activity.ActivityNetworkRepositoryInterface
import com.example.readytoenjoy.core.data.network.activity.model.toExternal
import com.example.readytoenjoy.core.model.Activity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @class DefaultActivityRepository
 * @brief Repositorio principal para la gestión de actividades
 * @details Implementa el patrón Repository combinando fuentes de datos remotas y locales.
 *          Proporciona funcionalidades offline-first con sincronización automática.
 */
@Singleton
class DefaultActivityRepository @Inject constructor(
    private val remote: ActivityNetworkRepositoryInterface,
    private val local: ActivityLocal
): ActivityRepositoryInterface {

    /** @brief Estado interno para mantener la lista actual de actividades */
    private val _state = MutableStateFlow<List<Activity>>(listOf())

    /**
     * @brief Obtiene todas las actividades con estrategia offline-first
     * @details Intenta obtener datos del servidor, si falla usa caché local
     * @return Result<List<Activity>> Lista de actividades o error
     */
    override suspend fun getActivities(): Result<List<Activity>> {
        return try {
            val networkResult = remote.getActivities()

            if (networkResult.isSuccess) {
                val activities = networkResult.getOrNull() ?: emptyList()
                updateLocalCache(activities)
                _state.value = activities
                Result.success(activities)
            } else {
                val localResult = local.readAll()
                if (localResult.isSuccess) {
                    val activities = localResult.getOrNull() ?: emptyList()
                    _state.value = activities
                    Result.success(activities)
                } else {
                    networkResult
                }
            }
        } catch (e: Exception) {
            val localResult = local.readAll()
            if (localResult.isSuccess) {
                val activities = localResult.getOrNull() ?: emptyList()
                _state.value = activities
                Result.success(activities)
            } else {
                Result.failure(e)
            }
        }
    }

    /**
     * @brief Obtiene actividades filtradas por ID de aventurero
     * @details Recupera actividades específicas de un aventurero con fallback offline
     * @param advenId ID del aventurero
     * @return Result<List<Activity>> Lista filtrada de actividades
     */
    override suspend fun getActivitiesByAdvenId(advenId: String): Result<List<Activity>> {
        return try {
            val networkResult = remote.getActivitiesByAdvenId(advenId)

            if (networkResult.isSuccess) {
                val activities = networkResult.getOrNull() ?: emptyList()
                updateLocalCacheForAdven(activities)
                Result.success(activities)
            } else {
                val localResult = local.readAll()
                if (localResult.isSuccess) {
                    val allActivities = localResult.getOrNull() ?: emptyList()
                    val filteredActivities = allActivities.filter { it.advenId == advenId }
                    Result.success(filteredActivities)
                } else {
                    networkResult
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * @brief Obtiene una actividad específica por ID
     * @details Intenta primero desde red, luego desde caché local
     * @param id ID único de la actividad
     * @return Result<Activity> La actividad encontrada o error
     */
    override suspend fun getOne(id: String): Result<Activity> {
        return try {
            val networkResult = remote.readOne(id)

            if (networkResult.isSuccess) {
                val activity = networkResult.getOrNull()
                activity?.let {
                    local.createOne(it)
                }
                networkResult
            } else {
                local.readOne(id)
            }
        } catch (e: Exception) {
            local.readOne(id)
        }
    }

    /**
     * @brief Crea una nueva actividad
     * @details Crea la actividad en el servidor y actualiza caché local
     * @param title Título de la actividad
     * @param img URI de la imagen (opcional)
     * @param location Ubicación de la actividad
     * @param price Precio de la actividad
     * @param description Descripción detallada
     * @param advenId ID del aventurero propietario
     * @return Result<Activity> La actividad creada o error
     */
    override suspend fun createActivity(
        title: String,
        img: Uri?,
        location: String,
        price: String,
        description: String,
        advenId: String?
    ): Result<Activity> {
        return try {
            val result = remote.createActivity(title, location, price, description, img, advenId)

            if (result.isSuccess) {
                val activity = result.getOrNull()
                activity?.let {
                    local.createOne(it)
                    val currentList = _state.value.toMutableList()
                    currentList.add(it)
                    _state.value = currentList
                }
            }
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * @brief Actualiza una actividad existente
     * @details Actualiza la actividad en el servidor y sincroniza con caché local
     * @param id ID de la actividad a actualizar
     * @param title Nuevo título
     * @param img Nueva imagen (opcional)
     * @param location Nueva ubicación
     * @param price Nuevo precio
     * @param description Nueva descripción
     * @return Activity La actividad actualizada
     * @throws Exception Si la actualización falla
     */
    override suspend fun updateActivity(
        id: String,
        title: String,
        img: Uri?,
        location: String,
        price: String,
        description: String
    ): Activity {
        val response = remote.updateActivity(id, title, location, price, description, img)

        if (response.isSuccessful) {
            var updatedActivity = response.body()!!.data.toExternal()

            if (img != null) {
                val refreshResult = remote.readOne(id)
                if (refreshResult.isSuccess) {
                    updatedActivity = refreshResult.getOrNull()!!
                }
            }

            local.createOne(updatedActivity)

            val currentList = _state.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == id }
            if (index != -1) {
                currentList[index] = updatedActivity
                _state.value = currentList
            }

            return updatedActivity
        } else {
            throw Exception("Error al actualizar la actividad")
        }
    }

    /**
     * @brief Elimina una actividad
     * @details Elimina la actividad del servidor y actualiza caché local
     * @param id ID de la actividad a eliminar
     * @return Result<Boolean> true si se eliminó correctamente, error en caso contrario
     */
    override suspend fun deleteActivity(id: String): Result<Boolean> {
        return try {
            val result = remote.deleteActivity(id)

            if (result.isSuccess) {
                local.deleteActivity(id)
                Log.d("ROOM_DEBUG", "Room eliminó actividad")

                val currentList = _state.value.toMutableList()
                val activityToRemove = currentList.find { activity -> activity.id == id }
                if (activityToRemove != null) {
                    currentList.remove(activityToRemove)
                    _state.value = currentList
                }

                refreshCacheFromServer()
            }
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * @brief Proporciona un stream reactivo de actividades
     * @details Observa cambios en las actividades con sincronización automática
     * @return Flow<Result<List<Activity>>> Stream reactivo de actividades
     */
    override fun setStream(): Flow<Result<List<Activity>>> {
        return local.observeAll()
            .onStart {
                try {
                    val networkResult = remote.getActivities()
                    if (networkResult.isSuccess) {
                        val activities = networkResult.getOrNull() ?: emptyList()
                        updateLocalCache(activities)
                    }
                } catch (e: Exception) {
                    // Ignore and use local cache
                }
            }
    }

    /**
     * @brief Refresca el caché desde el servidor
     * @details Sincroniza el caché local con los datos más recientes del servidor
     */
    private suspend fun refreshCacheFromServer() {
        try {
            val networkResult = remote.getActivities()
            if (networkResult.isSuccess) {
                val activities = networkResult.getOrNull() ?: emptyList()

                local.clearAll()
                local.updateActivities(activities)
                _state.value = activities
            }
        } catch (e: Exception) {
            // Silently fail - local cache remains unchanged
        }
    }

    /**
     * @brief Actualiza el caché local con nuevas actividades
     * @details Reemplaza completamente el caché local con nuevos datos
     * @param activities Lista de actividades para actualizar el caché
     */
    private suspend fun updateLocalCache(activities: List<Activity>) {
        try {
            local.updateActivities(activities)
        } catch (e: Exception) {
            // Silently fail - operation continues without local cache
        }
    }

    /**
     * @brief Actualiza el caché local para un aventurero específico
     * @details Añade actividades específicas al caché local
     * @param activities Lista de actividades del aventurero
     */
    private suspend fun updateLocalCacheForAdven(activities: List<Activity>) {
        try {
            activities.forEach { activity ->
                local.createOne(activity)
            }
        } catch (e: Exception) {
            // Silently fail - operation continues without local cache
        }
    }
}