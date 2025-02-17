package com.example.readytoenjoy.core.data.local.database.adven

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Adventurous")
data class AdvenEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
)
