package com.example.readytoenjoy.core.network

import com.example.readytoenjoy.core.network.activity.ActivityAttributesResponse
import com.example.readytoenjoy.core.network.activity.ActivityListRawResponse
import com.example.readytoenjoy.core.network.activity.ActivityRawResponse
import com.example.readytoenjoy.core.network.activity.CreateActivity
import com.example.readytoenjoy.core.network.adevn.AdvenListRawResponse
import com.example.readytoenjoy.core.network.adevn.AdvenRawResponse
import com.example.readytoenjoy.core.network.adevn.AdvenRequest
import com.example.readytoenjoy.core.network.adevn.UserRequest
import com.example.readytoenjoy.core.network.adevn.AdvenResponseLR
import com.example.readytoenjoy.core.network.adevn.userResponseLR
import com.example.readytoenjoy.core.network.adevn.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReadyToEnjoyApiService {

    //muestra las actividades
    @GET("activities")
    suspend fun getAllActivitiesFromSercice(): Response<ActivityListRawResponse>

    //quiero obtener las actividades de el usuario registrado
    @GET("activities?filters[advenId][id]={advenId}")
    suspend fun getAllMyActivitiesFromSercice(@Path("advenId") advenId: String): Response<ActivityListRawResponse>


    //
    @GET("activities/{id}")
    suspend fun readOneFomService(@Path("id") id: String): Response<ActivityRawResponse>

    //muestra aventureros
    @GET("adventurers")
    suspend fun getAllAdvensFromSercice(): Response<AdvenListRawResponse>

    //
    @GET("adventurers/id")
    suspend fun readOneFromService(@Path("id") id: String): Response<AdvenRawResponse>

    //
    @GET("adventurers")
    suspend fun readUser(@Query("email")email:String): Response<List<UserRequest>>

    //creacion de actividades
    @POST("activities")
    suspend fun cretaeActivities(@Body activity: CreateActivity):Response<ActivityRawResponse>

    //registro
    @POST("auth/local/register")
    suspend fun register(@Body userRequest: UserRequest): Response<userResponseLR>

    //registroadven
    @POST("adventurers")
    suspend fun registerAdven(@Body advenRequest: AdvenRequest): Response<AdvenRawResponse>

    //login
    @POST("auth/local")
    suspend fun login(@Body loginData: LoginRequest): Response<userResponseLR>

}