package com.example.readytoenjoy.core.network.adevn

import com.example.readytoenjoy.core.data.adven.Adven
import retrofit2.Response

interface AdvenNetworkRepositoryInterface {
    suspend fun readAdven(): Response<AdvenListRawResponse>
    suspend fun readOneAdven(id: String): Response<Adven>
}