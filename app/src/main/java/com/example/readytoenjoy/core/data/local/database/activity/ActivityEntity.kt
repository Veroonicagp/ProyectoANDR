package com.example.readytoenjoy.core.data.local.database.activity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity")
data class ActivityEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val img: String?,
    val location: String,
    val price: String,
    val description: String,
    val advenId: String?
)
