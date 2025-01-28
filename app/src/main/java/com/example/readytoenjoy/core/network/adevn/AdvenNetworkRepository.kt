package com.example.readytoenjoy.core.network.adevn

import com.example.readytoenjoy.core.data.adven.Adven
import com.example.readytoenjoy.core.network.ReadyToEnjoyApiService
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