package com.example.readytoenjoy.core.network.activity

import android.net.Uri
import com.example.readytoenjoy.core.data.activity.Activity
import retrofit2.Response

interface ActivityNetworkRepositoryInterface {
    suspend fun getActivities(): Result<List<Activity>>
    suspend fun getActivitiesByAdvenId(id: String): Result<List<Activity>>
    suspend fun createActivity(title: String,location:String, price:String, description:String):Result<Activity>
    suspend fun readOne(id: String): Result<Activity>
}