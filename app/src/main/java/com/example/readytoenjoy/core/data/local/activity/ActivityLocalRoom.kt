package com.example.readytoenjoy.core.data.local.activity

import com.example.readytoenjoy.core.data.local.database.activity.ActivityDao
import com.example.readytoenjoy.core.model.Activity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ActivityLocalRoom @Inject constructor(
    private val dao: ActivityDao
): ActivityLocal {
    override suspend fun readAll(): Result<List<Activity>> {
        TODO("Not yet implemented")
    }

    override suspend fun readOne(id: String): Result<Activity> {
        TODO("Not yet implemented")
    }

    override suspend fun createOne(activity: Activity): Result<Activity> {
        TODO("Not yet implemented")
    }

    override fun observeAll(): Flow<Result<List<Activity>>> {
        TODO("Not yet implemented")
    }
}