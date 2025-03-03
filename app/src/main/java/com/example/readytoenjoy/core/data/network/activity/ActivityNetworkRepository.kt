package com.example.readytoenjoy.core.data.network.activity

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.core.data.network.ReadyToEnjoyApiService
import com.example.readytoenjoy.core.data.network.activity.model.ActivityRequest
import com.example.readytoenjoy.core.data.network.activity.model.ActivityData
import com.example.readytoenjoy.core.data.network.activity.model.ActivityRawResponse
import com.example.readytoenjoy.core.data.network.activity.model.toExternal
import com.example.readytoenjoy.core.data.network.activity.model.toModel
import com.example.readytoenjoy.core.data.network.activity.model.toRemoteModel
import com.example.readytoenjoy.di.NetworkServiceModule
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
            Result.failure(UserNotAuthorizedException())
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
        advenId: String?
    ): Result<Activity> {
        val newActivity = ActivityRequest(
            ActivityData(
                title = title,
                location = location,
                price= price,
                description= description,
                advenId= advenId,
            )
        )

        val response = api.cretaeActivities(newActivity)

        if (response.isSuccessful) {
            var uploadedActivity = response.body()!!.data.toExternal()
            img?.let { uri ->
                val imageUploaded = uploadActivity(uri,response.body()!!.data.id)
                // Si ha subido obtenemos la Uri
                if( imageUploaded.isSuccess) {
                    val uploadedUri = imageUploaded.getOrNull()!!
                    uploadedActivity = uploadedActivity.copy(
                        img = uploadedUri
                    )
                }

            }
            return Result.success(uploadedActivity)
        } else {
            return Result.failure(UserNotAuthorizedException())
        }


    }


    override suspend fun readOne(id: String): Result<Activity> {
        val response = api.readOneActFomService(id)
        return if (response.isSuccessful) {
            Result.success(response.body()!!.data.toModel())
        } else {
            return Result.failure(UserNotAuthorizedException())
        }
    }

    override suspend fun updateActivity(
        id: String,
        title: String,
        location: String,
        price: String,
        description: String,
        img: Uri?
    ): Response<ActivityRawResponse> {

        val currentActivity =  readOne(id).getOrNull()!!

        val updatedActivity = Activity(
            id = id,
            title = title,
            location = location,
            price = price,
            description = description,
            img = img,
            advenId = currentActivity.advenId
        )

        val activityRequest = updatedActivity.toRemoteModel()

        return api.updateActivity(id, activityRequest)
    }

    override suspend fun deleteActivity(id: String): Result<Boolean> {
        val response = api.deleteActivity(id)
        if (response.isSuccessful) {
            return Result.success(true)
        } else {
            return Result.failure(UserNotAuthorizedException())
        }

    }

    private suspend fun uploadActivity(
        uri: Uri,
        activityId: String,
    ): Result<Uri> {
        try {
            android.util.Log.d("ImageUpload", "Starting upload for activity ID: $activityId")

            // Obtenemos el resolver de MediaStore
            val resolver = context.contentResolver
            // Abrimos el input stream a partir de la URI
            val inputStream = resolver.openInputStream(uri)
                ?: throw IllegalArgumentException("Cannot open InputStream from Uri")
            // Obtenemos el tipo del fichero
            val mimeType = resolver.getType(uri) ?: "image/*"
            // Obtenemos el nombre local, esto podiamos cambiarlo a otro patrón
            val fileName = uri.lastPathSegment ?: "activity.jpg"
            // Convertimos el fichero a cuerpo de la petición
            val requestBody = inputStream.readBytes().toRequestBody(mimeType.toMediaTypeOrNull())
            // Construimos la parte de la petición
            val part = MultipartBody.Part.createFormData("files", fileName, requestBody)
            // Map con el resto de parámetros
            val partMap: MutableMap<String, RequestBody> = mutableMapOf()

            // Referencia
            partMap["ref"] = "api::activity.activity".toRequestBody("text/plain".toMediaType())
            // Id del incidente
            partMap["refId"] = activityId.toRequestBody("text/plain".toMediaType())
            // Campo de la colección
            partMap["field"] = "img".toRequestBody("text/plain".toMediaType())
            android.util.Log.d("ImageUpload", "Request params: ref=api::activity.activity, refId=$activityId, field=img")
            // Subimos el fichero
            val imageResponse = api.addActivityImg(
                partMap,
                files = part,
            )

            android.util.Log.d("ImageUpload", "Response code: ${imageResponse.code()}")
            // Si ha ido mal la subida, salimos con error
            if (!imageResponse.isSuccessful) {
                android.util.Log.e("ImageUpload", "Error uploading image: ${imageResponse.errorBody()?.string()}")
                return Result.failure(UserNotAuthorizedException())
            }
            else {
                val responseBody = imageResponse.body()
                android.util.Log.d("ImageUpload", "Response body: $responseBody")

                if (responseBody == null || responseBody.isEmpty()) {
                    return Result.failure(Exception("Empty response body"))
                }

                val remoteUri= "${NetworkServiceModule.STRAPI}${imageResponse.body()!!.first().formats.small.url}"
                android.util.Log.d("ImageUpload", "Generated URI: $remoteUri")
                return Result.success(remoteUri.toUri())
            }
        } catch (e: Exception) {
            android.util.Log.e("ImageUpload", "Error uploading image", e)
            return Result.failure(e)
        }
    }


}

class UserNotAuthorizedException :RuntimeException() {
    override fun toString() = "Error"
}