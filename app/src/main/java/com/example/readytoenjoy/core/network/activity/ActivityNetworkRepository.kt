package com.example.readytoenjoy.core.network.activity

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.example.readytoenjoy.core.data.activity.Activity
import com.example.readytoenjoy.core.network.ReadyToEnjoyApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
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
        //img: Uri?,
        location: String,
        price: String,
        description: String,
        //advenId: String?
    ): Result<Activity> {
        val newActivity = CreateActivity(
            CreateActivityPayload(
                title,location,price,description
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