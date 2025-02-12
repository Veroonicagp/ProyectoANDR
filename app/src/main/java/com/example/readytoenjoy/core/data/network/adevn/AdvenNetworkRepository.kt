package com.example.readytoenjoy.core.data.network.adevn

import com.example.readytoenjoy.core.model.Adven
import com.example.readytoenjoy.core.data.network.ReadyToEnjoyApiService
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenListRawResponse
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

    override suspend fun readOneAdven(id: String): Response<Adven> {
        TODO("Not yet implemented")
    }


}