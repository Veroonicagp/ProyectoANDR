package com.example.readytoenjoy.core.network


import com.example.readytoenjoy.core.data.adven.Adven
import com.example.readytoenjoy.core.network.activity.ActivityListRawResponse
import com.example.readytoenjoy.core.network.activity.ActivityRawResponse
import com.example.readytoenjoy.core.network.adevn.AdvenListRawResponse
import com.example.readytoenjoy.core.network.adevn.AdvenRawResponse
import com.example.readytoenjoy.core.network.adevn.AdvenRequest
import com.example.readytoenjoy.core.network.adevn.AdvenResponseLR
import com.example.readytoenjoy.core.network.adevn.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReadyToEnjoyApiService {
    @GET("activities")
    suspend fun getAllActivitiesFromSercice(): Response<ActivityListRawResponse>

    @GET("activities/{id}")
    suspend fun readOneFomService(@Path("id") id: String): Response<ActivityRawResponse>

    @GET("adventurers")
    suspend fun getAllAdvensFromSercice(): Response<AdvenListRawResponse>

    @GET("adventurers/id")
    suspend fun readOneFromService(@Path("id") id: String): Response<AdvenRawResponse>

    @GET("adventurers")
    suspend fun readUser(@Query("email")email:String): Response<List<AdvenRequest>>

    //registro
    @POST("auth/local/register")
    suspend fun register(@Body advenRequest: AdvenRequest): Response<AdvenResponseLR>

    //login
    @POST("auth/local")
    suspend fun login(@Body loginData: LoginRequest): Response<AdvenResponseLR>

}