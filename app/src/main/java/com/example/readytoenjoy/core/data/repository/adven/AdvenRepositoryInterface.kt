package com.example.readytoenjoy.core.data.repository.adven

import android.net.Uri
import com.example.readytoenjoy.core.model.Adven
import kotlinx.coroutines.flow.StateFlow

interface AdvenRepositoryInterface {

    suspend fun getAdvens(): List<Adven>
    suspend fun getOne(id:String): Adven
    suspend fun updateAdven(id: String, media: Uri?, name: String, email: String): Adven

    val setStream: StateFlow<List<Adven>>
}