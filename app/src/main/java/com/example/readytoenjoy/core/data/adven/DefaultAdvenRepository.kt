package com.example.readytoenjoy.core.data.adven

import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class DefaultAdvenRepository @Inject constructor():AdvenRepositoryInterface {
    override suspend fun getAdvens(): List<Adven> {
        TODO("Not yet implemented")
    }

    override suspend fun getOne(id: String): Adven {
        TODO("Not yet implemented")
    }

    override val setStream: StateFlow<List<Adven>>
        get() = TODO("Not yet implemented")
}