package com.example.readytoenjoy.core.data.local.activity

import android.util.Log
import com.example.readytoenjoy.core.data.local.database.activity.ActivityDao
import com.example.readytoenjoy.core.data.local.database.toLocalModel
import com.example.readytoenjoy.core.data.local.database.toLocalEntity
import com.example.readytoenjoy.core.model.Activity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ActivityLocalRoom @Inject constructor(
    private val dao: ActivityDao
): ActivityLocal {

    override suspend fun readAll(): Result<List<Activity>> {
        return try {
            val entities = dao.readAllActivities()
            val activities = entities.map { it.toLocalModel() }
            Log.d("ROOM_DEBUG", "Room readAll: ${activities.size} actividades")
            Result.success(activities)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun readOne(id: String): Result<Activity> {
        return try {
            val entity = dao.readOne(id)
            if (entity != null) {
                Result.success(entity.toLocalModel())
            } else {
                Result.failure(Exception("Activity not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createOne(activity: Activity): Result<Activity> {
        return try {
            val entity = activity.toLocalEntity()
            dao.insertIncident(entity)
            Result.success(activity)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeAll(): Flow<Result<List<Activity>>> {
        return dao.observeActivities().map { entities ->
            try {
                val activities = entities.map { it.toLocalModel() }
                Log.d("ROOM_DEBUG", "Room observeAll: ${activities.size} actividades")
                Result.success(activities)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun deleteActivity(activityId: String): Result<Unit> {
        return try {
            dao.deleteActivityById(activityId)

            val remaining = dao.readAllActivities()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteActivitiesByAdvenId(advenId: String): Result<Unit> {
        return try {
            dao.deleteActivitiesByAdvenId(advenId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun clearAll(): Result<Unit> {
        return try {
            dao.deleteAllActivities()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateActivities(activities: List<Activity>): Result<Unit> {
        return try {
            dao.deleteAllActivities()
            val entities = activities.map { it.toLocalEntity() }
            dao.insertAll(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}