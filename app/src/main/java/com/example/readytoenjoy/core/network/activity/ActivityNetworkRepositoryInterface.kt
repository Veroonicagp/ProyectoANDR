package com.example.readytoenjoy.core.network.activity

import com.example.readytoenjoy.core.data.activity.Activity
import retrofit2.Response

interface ActivityNetworkRepositoryInterface {
    suspend fun getActivities(): Response<ActivityListRawResponse>
    suspend fun getActivitiesByAdvenId(id: String): Response<ActivityListRawResponse>
    suspend fun readOne(id: String): Response<Activity>
}