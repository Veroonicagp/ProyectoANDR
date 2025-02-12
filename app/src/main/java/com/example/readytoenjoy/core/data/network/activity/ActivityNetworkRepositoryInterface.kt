package com.example.readytoenjoy.core.data.network.activity

import com.example.readytoenjoy.core.model.Activity

interface ActivityNetworkRepositoryInterface {
    suspend fun getActivities(): Result<List<Activity>>
    suspend fun getActivitiesByAdvenId(id: String): Result<List<Activity>>
    suspend fun createActivity(title: String,location:String, price:String, description:String):Result<Activity>
    suspend fun readOne(id: String): Result<Activity>
}