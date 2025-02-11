package com.example.readytoenjoy.core.data.activity

import android.net.Uri
import kotlinx.coroutines.flow.Flow

//mirar diferencia State flow y flow
interface ActivityRepositoryInterface {

    suspend fun getActivities(): Result<List<Activity>>
    //para myActivities

    suspend fun getActivitiesByAdvenId(advenId: String): Result<List<Activity>>

    suspend fun getOne(id:String): Result<Activity>

    suspend fun createActivity(title: String,
                               //img: Uri?,
                               location: String,
                               price: String,
                               description: String,
                               //advenId: String?
    ): Result<Activity>

    fun setStream(): Flow<Result<List<Activity>>>
}