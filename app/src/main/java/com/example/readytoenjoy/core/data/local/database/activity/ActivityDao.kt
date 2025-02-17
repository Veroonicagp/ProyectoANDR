package com.example.readytoenjoy.core.data.local.database.activity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Insert
    suspend fun insertIncident(activity: ActivityEntity)
    @Query("SELECT * FROM activity")
    suspend fun readAllActivities():List<ActivityEntity>
    @Query("SELECT * FROM activity")
    fun observeActivities(): Flow<List<ActivityEntity>>
    @Query("SELECT * FROM activity WHERE id = :id")
    suspend fun readOne(id:String):ActivityEntity?
}