package com.example.readytoenjoy.core.data.network

import com.example.readytoenjoy.core.data.network.activity.model.ActivityListRawResponse
import com.example.readytoenjoy.core.data.network.activity.model.ActivityRawResponse
import com.example.readytoenjoy.core.data.network.activity.model.ActivityRequest
import com.example.readytoenjoy.core.data.network.activity.model.CreatedMediaItemResponse
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenListRawResponse
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenRawResponse
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenRequest
import com.example.readytoenjoy.core.data.network.adevn.model.UserRequest
import com.example.readytoenjoy.core.data.network.adevn.model.userResponseLR
import com.example.readytoenjoy.core.data.network.adevn.model.LoginRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface ReadyToEnjoyApiService {

////////REGISTRO Y LOGIN/////

    //registro
    @POST("auth/local/register")
    suspend fun register(@Body userRequest: UserRequest): Response<userResponseLR>

    //login
    @POST("auth/local")
    suspend fun login(@Body loginData: LoginRequest): Response<userResponseLR>

/////////AVENTUREROS/////

    //registroadven
    @POST("adventurers")
    suspend fun registerAdven(@Body advenRequest: AdvenRequest): Response<AdvenRawResponse>

    //obtiene el aventurero con el userId asociado
    @GET("adventurers")
    suspend fun getAdvenByUserId(@Query("filters[user]") userId: String): Response<AdvenListRawResponse>

    //muestra aventureros
    @GET("adventurers")
    suspend fun getAllAdvensFromSercice(@Query("populate") populate: String = "media"): Response<AdvenListRawResponse>

    //muestra la info del aventurero
    @GET("adventurers/{id}")
    suspend fun readOneAdvenFromService(@Path("id") id: String): Response<AdvenRawResponse>

    //edición aventureros
    @PUT("adventurers/{id}")
    suspend fun updateAdven( @Path("id") id: String, @Body advenRequest: AdvenRequest): Response<AdvenRawResponse>


////////ACTIVITIES/////

    //creacion de actividades
    @POST("activities")
    suspend fun cretaeActivities(@Body activity: ActivityRequest):Response<ActivityRawResponse>

    //quiero obtener las actividades de el usuario registrado
    @GET("activities")
    suspend fun getAllMyActivitiesFromSercice(@Query("filters[advenId]") advenId: String, @Query("populate") populate: String = "img"): Response<ActivityListRawResponse>

    //muestra las actividades
    @GET("activities")
    suspend fun getAllActivitiesFromSercice(
        @Query("populate") populate: String = "img",
    ): Response<ActivityListRawResponse>

    //muestra una actividad en especifico
    @GET("activities/{id}")
    suspend fun readOneActFomService(@Path("id") id: String,
                                     @Query("populate") populate: String = "img"): Response<ActivityRawResponse>

    //edición actividades
    @PUT("activities/{id}")
    suspend fun updateActivity(@Path("id") id: String, @Body activityRequest: ActivityRequest): Response<ActivityRawResponse>

    //elimina
    @DELETE("activities/{id}")
    suspend fun deleteActivity(@Path("id") id: String): Response<Void>

    //subida de fotos
    @Multipart
    @POST("upload")
    suspend fun addActivityImg(@PartMap partMap: MutableMap<String, RequestBody>,
                               @Part files: MultipartBody.Part ):Response<List<CreatedMediaItemResponse>>






}