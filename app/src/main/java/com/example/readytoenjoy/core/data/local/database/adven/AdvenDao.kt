package com.example.readytoenjoy.core.data.local.database.adven

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AdvenDao {

    @Insert
    suspend fun insertAdven(adven:AdvenEntity)

    @Query("SELECT * FROM adventurous")
    suspend fun readAllAdven():List<AdvenEntity>
    @Query("SELECT * FROM adventurous")
    fun observeAdven(): Flow<List<AdvenEntity>>
    @Query("SELECT * FROM adventurous WHERE id = :id")
    suspend fun  readOne(id:String):AdvenEntity
}