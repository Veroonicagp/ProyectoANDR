package com.example.readytoenjoy.core.data.network.adevn

import android.net.Uri
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenListRawResponse
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenRawResponse
import com.example.readytoenjoy.core.model.Adven
import retrofit2.Response

interface AdvenNetworkRepositoryInterface {
    suspend fun readAdven(): Response<AdvenListRawResponse>
    suspend fun readOneAdven(id: String): Response<AdvenRawResponse>
    suspend fun updateAdven(id: String, img: Uri?, name: String, email: String): Response<AdvenRawResponse>
}