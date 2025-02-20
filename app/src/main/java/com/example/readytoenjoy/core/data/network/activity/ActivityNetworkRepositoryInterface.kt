package com.example.readytoenjoy.core.data.network.activity

import android.net.Uri
import com.example.readytoenjoy.core.data.network.activity.model.ActivityRawResponse
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenRawResponse
import com.example.readytoenjoy.core.model.Activity
import retrofit2.Response

interface ActivityNetworkRepositoryInterface {
    suspend fun getActivities(): Result<List<Activity>>
    suspend fun getActivitiesByAdvenId(id: String): Result<List<Activity>>
    suspend fun createActivity(title: String,location:String, price:String, description:String, img: Uri?, advenId:String?):Result<Activity>
    suspend fun readOne(id: String): Result<Activity>
    suspend fun updateActivity(id: String, title: String,location:String, price:String, description:String, /**img: Uri?**/): Response<ActivityRawResponse>
}