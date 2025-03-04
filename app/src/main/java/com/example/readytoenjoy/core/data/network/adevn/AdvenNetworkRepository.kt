package com.example.readytoenjoy.core.data.network.adevn

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.example.readytoenjoy.core.data.network.ReadyToEnjoyApiService
import com.example.readytoenjoy.core.data.network.activity.UserNotAuthorizedException
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenListRawResponse
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenRawResponse
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenRequest
import com.example.readytoenjoy.core.data.network.adevn.model.AventureroData
import com.example.readytoenjoy.di.NetworkServiceModule
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdvenNetworkRepository @Inject constructor(
    private val api: ReadyToEnjoyApiService,
    @ApplicationContext private val context: Context,
): AdvenNetworkRepositoryInterface {

    override suspend fun readAdven(): Response<AdvenListRawResponse> {
        return api.getAllAdvensFromSercice()
    }

    override suspend fun readOneAdven(id: String): Response<AdvenRawResponse> {
        return api.readOneAdvenFromService(id)
    }

    override suspend fun updateAdven(
        id: String,
        media: Uri?,
        name: String,
        email: String,
    ): Response<AdvenRawResponse> {
        val advenRequest = AdvenRequest(
            data = AventureroData(
                name = name,
                email = email,
                media = null,
                password = "",
                user = ""
            )
        )
        val response = api.updateAdven(id, advenRequest)
        if (media != null && response.isSuccessful) {
                val uploadResult = uploadAdven(media, id)
                if (uploadResult.isFailure) {
                    android.util.Log.e("ImageUpdate", "Failed to upload image: ${uploadResult.exceptionOrNull()?.message}")
                } else {
                    android.util.Log.d("ImageUpdate", "Image uploaded successfully: ${uploadResult.getOrNull()}")
                    return api.readOneAdvenFromService(id)
                }
        }
        return response
    }

    private suspend fun uploadAdven(
        uri: Uri,
        advenId: String,
    ): Result<Uri> {
        try {
            android.util.Log.d("ImageUpload", "Starting upload for adven ID: $advenId")

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
            partMap["ref"] = "api::adventurous.adventurous".toRequestBody("text/plain".toMediaType())
            // Id del incidente
            partMap["refId"] = advenId.toRequestBody("text/plain".toMediaType())
            // Campo de la colección
            partMap["field"] = "media".toRequestBody("text/plain".toMediaType())
            android.util.Log.d("ImageUpload", "Request params: ref=api::adventurous.adventurous, refId=$advenId, field=media")
            // Subimos el fichero
            val imageResponse = api.addImg(
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
            return Result.failure(e)
        }
    }


}
