package com.example.readytoenjoy.core.data.local.database.activity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(activity: ActivityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(activities: List<ActivityEntity>)

    @Query("SELECT * FROM activity")
    suspend fun readAllActivities(): List<ActivityEntity>

    @Query("SELECT * FROM activity")
    fun observeActivities(): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activity WHERE id = :id")
    suspend fun readOne(id: String): ActivityEntity?

    @Query("SELECT * FROM activity WHERE advenId = :advenId")
    suspend fun readActivitiesByAdvenId(advenId: String): List<ActivityEntity>

    @Query("SELECT * FROM activity WHERE advenId = :advenId")
    fun observeActivitiesByAdvenId(advenId: String): Flow<List<ActivityEntity>>

    @Update
    suspend fun updateActivity(activity: ActivityEntity)

    @Delete
    suspend fun deleteActivity(activity: ActivityEntity)

    @Query("DELETE FROM activity WHERE id = :id")
    suspend fun deleteActivityById(id: String)

    @Query("DELETE FROM activity WHERE advenId = :advenId")
    suspend fun deleteActivitiesByAdvenId(advenId: String)

    @Query("DELETE FROM activity")
    suspend fun deleteAllActivities()
}