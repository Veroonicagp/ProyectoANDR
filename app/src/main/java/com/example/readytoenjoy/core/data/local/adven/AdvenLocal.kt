package com.example.readytoenjoy.core.data.local.adven

import com.example.readytoenjoy.core.model.Adven
import kotlinx.coroutines.flow.Flow

interface AdvenLocal {
    suspend fun readAll(): Result<List<Adven>>
    suspend fun readOne(id: String): Result<Adven>
    suspend fun createOne(adven: Adven): Result<Adven>
    fun observeAll(): Flow<Result<List<Adven>>>
}