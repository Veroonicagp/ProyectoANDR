/**
 * @file ActivityDao.kt
 * @brief DAO para las operaciones de base de datos de actividades
 * @details Define las operaciones CRUD para la entidad Activity en Room Database
 * @author ReadyToEnjoy Team
 * @version 1.0
 */

package com.example.readytoenjoy.core.data.local.database.activity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * @interface ActivityDao
 * @brief DAO (Data Access Object) para operaciones de base de datos de actividades
 * @details Proporciona métodos para crear, leer, actualizar y eliminar actividades en Room
 */
@Dao
interface ActivityDao {

    /**
     * @brief Inserta una actividad en la base de datos
     * @details Si existe conflicto con el ID, reemplaza la actividad existente
     * @param activity Entidad de actividad a insertar
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(activity: ActivityEntity)

    /**
     * @brief Inserta múltiples actividades en la base de datos
     * @details Operación en lote para insertar varias actividades de una vez
     * @param activities Lista de entidades de actividad a insertar
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(activities: List<ActivityEntity>)

    /**
     * @brief Obtiene todas las actividades de la base de datos
     * @details Recupera todas las actividades almacenadas localmente
     * @return List<ActivityEntity> Lista de todas las actividades
     */
    @Query("SELECT * FROM activity")
    suspend fun readAllActivities(): List<ActivityEntity>

    /**
     * @brief Observa cambios en todas las actividades
     * @details Proporciona un Flow que emite cambios en tiempo real
     * @return Flow<List<ActivityEntity>> Stream de actividades que se actualiza automáticamente
     */
    @Query("SELECT * FROM activity")
    fun observeActivities(): Flow<List<ActivityEntity>>

    /**
     * @brief Obtiene una actividad específica por su ID
     * @details Busca una actividad usando su identificador único
     * @param id ID de la actividad a buscar
     * @return ActivityEntity? La actividad encontrada o null si no existe
     */
    @Query("SELECT * FROM activity WHERE id = :id")
    suspend fun readOne(id: String): ActivityEntity?

    /**
     * @brief Elimina una actividad por su ID
     * @details Elimina permanentemente una actividad de la base de datos
     * @param id ID de la actividad a eliminar
     */
    @Query("DELETE FROM activity WHERE id = :id")
    suspend fun deleteActivityById(id: String)

    /**
     * @brief Elimina todas las actividades de un aventurero específico
     * @details Elimina todas las actividades asociadas a un aventurero
     * @param advenId ID del aventurero cuyas actividades se van a eliminar
     */
    @Query("DELETE FROM activity WHERE advenId = :advenId")
    suspend fun deleteActivitiesByAdvenId(advenId: String)

    /**
     * @brief Elimina todas las actividades de la base de datos
     * @details Borra completamente la tabla de actividades
     */
    @Query("DELETE FROM activity")
    suspend fun deleteAllActivities()
}