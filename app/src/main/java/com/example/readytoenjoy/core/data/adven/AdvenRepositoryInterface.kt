package com.example.readytoenjoy.core.data.adven

import kotlinx.coroutines.flow.StateFlow

interface AdvenRepositoryInterface {

    suspend fun getAdvens(): List<Adven>
    suspend fun getOne(id:String): Adven

    val setStream: StateFlow<List<Adven>>
}