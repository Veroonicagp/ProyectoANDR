package com.example.readytoenjoy.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.readytoenjoy.core.data.local.database.activity.ActivityDao
import com.example.readytoenjoy.core.data.local.database.activity.ActivityEntity
import com.example.readytoenjoy.core.data.local.database.adven.AdvenDao
import com.example.readytoenjoy.core.data.local.database.adven.AdvenEntity

@Database(entities = [ActivityEntity::class,AdvenEntity::class], version = 1)
abstract class AppDatabase():RoomDatabase() {
    abstract fun activityDao(): ActivityDao
    abstract fun advenDao(): AdvenDao
}