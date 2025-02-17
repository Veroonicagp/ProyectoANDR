package com.example.readytoenjoy.core.data.local.activity

import com.example.readytoenjoy.core.model.Activity
import kotlinx.coroutines.flow.Flow

interface ActivityLocal {


    suspend fun readAll():Result<List<Activity>>
    suspend fun readOne(id:String):Result<Activity>
    suspend fun createOne(activity:Activity):Result<Activity>

    fun observeAll(): Flow<Result<List<Activity>>>
}