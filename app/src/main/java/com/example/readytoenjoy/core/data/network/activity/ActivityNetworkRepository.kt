package com.example.readytoenjoy.core.data.network.activity

import android.content.Context
import android.net.Uri
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.core.data.network.ReadyToEnjoyApiService
import com.example.readytoenjoy.core.data.network.activity.model.CreateActivity
import com.example.readytoenjoy.core.data.network.activity.model.CreateActivityPayload
import com.example.readytoenjoy.core.data.network.activity.model.toModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityNetworkRepository @Inject constructor(
    private val api: ReadyToEnjoyApiService,
    @ApplicationContext private val context: Context,
): ActivityNetworkRepositoryInterface {

    private val _state = MutableStateFlow<List<Activity>>(listOf())

    override suspend fun getActivities(): Result<List<Activity>> {
        val response = api.getAllActivitiesFromSercice()
        return if (response.isSuccessful) {
            Result.success(response.body()!!.data.toModel())
        }
        else {
            return when (response.code()) {
                403 -> {
                    // No autorizado
                    Result.failure(UserNotAuthorizedException())
                }

                else -> Result.failure(RuntimeException())
            }
        }
    }

    override suspend fun getActivitiesByAdvenId(id: String): Result<List<Activity>> {
        val response = api.getAllMyActivitiesFromSercice(id)

        if (response.isSuccessful) {
            return Result.success(response.body()!!.data.toModel())
        } else {
            // No se ha creado
            return Result.failure(UserNotAuthorizedException())
        }
    }

    override suspend fun createActivity(
        title: String,
        location: String,
        price: String,
        description: String,
        img: Uri?,
        //advenId: String?
    ): Result<Activity> {
        val newActivity = CreateActivity(
            CreateActivityPayload(
                title,location,price,description,/**advenId**/
            )
        )
        val response = api.cretaeActivities(newActivity)

        if (response.isSuccessful) {
            return Result.success(response.body()!!.data.toModel())
        } else {
            // No se ha creado
            return Result.failure(UserNotAuthorizedException())
        }


    }


    override suspend fun readOne(id: String): Result<Activity> {
        TODO()
        //return api.readOneFomService(id)
    }


}

class UserNotAuthorizedException :RuntimeException() {

    override fun toString() = "Incorrect identifier or password"
}