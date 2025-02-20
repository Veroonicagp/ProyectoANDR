package com.example.readytoenjoy.core.data.network.adevn

import com.example.readytoenjoy.core.data.network.ReadyToEnjoyApiService
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenListRawResponse
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenRawResponse
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenRequest
import com.example.readytoenjoy.core.data.network.adevn.model.AventureroData
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdvenNetworkRepository @Inject constructor(
    private val api: ReadyToEnjoyApiService
): AdvenNetworkRepositoryInterface {

    override suspend fun readAdven(): Response<AdvenListRawResponse> {
        return api.getAllAdvensFromSercice()
    }

    override suspend fun readOneAdven(id: String): Response<AdvenRawResponse> {
        return api.readOneAdvenFromService(id)
    }

    override suspend fun updateAdven(
        id: String,
        name: String,
        email: String
    ): Response<AdvenRawResponse> {
        val advenRequest = AdvenRequest(
            data = AventureroData(
                name = name,
                email = email,
                password = "", // No necesitamos actualizar la contraseña
                userId = "" // No necesitamos actualizar el userId
            )
        )
        return api.updateAdven(id, advenRequest)
    }


}