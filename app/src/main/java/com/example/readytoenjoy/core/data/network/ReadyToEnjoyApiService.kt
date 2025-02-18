package com.example.readytoenjoy.core.data.network

import com.example.readytoenjoy.core.data.network.activity.model.ActivityListRawResponse
import com.example.readytoenjoy.core.data.network.activity.model.ActivityRawResponse
import com.example.readytoenjoy.core.data.network.activity.model.CreateActivity
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenListRawResponse
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenRawResponse
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenRequest
import com.example.readytoenjoy.core.data.network.adevn.model.UserRequest
import com.example.readytoenjoy.core.data.network.adevn.model.userResponseLR
import com.example.readytoenjoy.core.data.network.adevn.model.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
    suspend fun getAdvenByUserId(@Query("filters[userId]") userId: String): Response<AdvenListRawResponse>

    //muestra aventureros
    @GET("adventurers")
    suspend fun getAllAdvensFromSercice(): Response<AdvenListRawResponse>

    //muestra la info del aventurero
    @GET("adventurers/id")
    suspend fun readOneFromService(@Path("id") id: String): Response<AdvenRawResponse>

    // TODO update

////////ACTIVITIES/////

    //muestra las actividades
    @GET("activities")
    suspend fun getAllActivitiesFromSercice(): Response<ActivityListRawResponse>

    //quiero obtener las actividades de el usuario registrado
    @GET("activities")
    suspend fun getAllMyActivitiesFromSercice(@Query("filters[advenId]") advenId: String): Response<ActivityListRawResponse>

    //muestra una actividad en especifico
    @GET("activities/{id}")
    suspend fun readOneFomService(@Path("id") id: String): Response<ActivityRawResponse>

    //creacion de actividades
    @POST("activities")
    suspend fun cretaeActivities(@Body activity: CreateActivity):Response<ActivityRawResponse>

    // TODO update






}