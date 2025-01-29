package com.example.readytoenjoy.core.network.activity

import com.example.readytoenjoy.core.data.activity.Activity
import com.example.readytoenjoy.core.network.ReadyToEnjoyApiService
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityNetworkRepository @Inject constructor(
    private val api: ReadyToEnjoyApiService
): ActivityNetworkRepositoryInterface {

    private val _state = MutableStateFlow<List<Activity>>(listOf())

    override suspend fun getActivities(): Response<ActivityListRawResponse> {
        return api.getAllActivitiesFromSercice()
    }

    override suspend fun getActivitiesByAdvenId(id: String): Response<ActivityListRawResponse> {
        return api.getAllMyActivitiesFromSercice(id)
    }

    override suspend fun readOne(id: String): Response<Activity> {
        TODO()
        //return api.readOneFomService(id)
    }


}