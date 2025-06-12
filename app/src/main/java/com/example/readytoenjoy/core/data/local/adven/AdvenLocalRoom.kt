package com.example.readytoenjoy.core.data.local.adven

import com.example.readytoenjoy.core.data.local.database.adven.AdvenDao
import com.example.readytoenjoy.core.data.local.database.toLocalModel
import com.example.readytoenjoy.core.data.local.database.toLocalEntity
import com.example.readytoenjoy.core.model.Adven
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AdvenLocalRoom @Inject constructor(
    private val dao: AdvenDao
): AdvenLocal {

    override suspend fun readAll(): Result<List<Adven>> {
        return try {
            val entities = dao.readAllAdven()
            val advens = entities.map { it.toLocalModel() }
            Result.success(advens)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun readOne(id: String): Result<Adven> {
        return try {
            val entity = dao.readOne(id)
            Result.success(entity.toLocalModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createOne(adven: Adven): Result<Adven> {
        return try {
            val entity = adven.toLocalEntity()
            dao.insertAdven(entity)
            Result.success(adven)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeAll(): Flow<Result<List<Adven>>> {
        return dao.observeAdven().map { entities ->
            try {
                val advens = entities.map { it.toLocalModel() }
                Result.success(advens)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}