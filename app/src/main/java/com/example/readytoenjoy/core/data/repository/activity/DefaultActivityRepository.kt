package com.example.readytoenjoy.core.data.repository.activity

import android.net.Uri
import android.util.Log
import com.example.readytoenjoy.core.data.local.activity.ActivityLocal
import com.example.readytoenjoy.core.data.network.activity.ActivityNetworkRepositoryInterface
import com.example.readytoenjoy.core.data.network.activity.model.toExternal
import com.example.readytoenjoy.core.model.Activity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultActivityRepository @Inject constructor(
    private val remote: ActivityNetworkRepositoryInterface,
    private val local: ActivityLocal
): ActivityRepositoryInterface {

    private val _state = MutableStateFlow<List<Activity>>(listOf())

    override suspend fun getActivities(): Result<List<Activity>> {
        return try {
            val networkResult = remote.getActivities()

            if (networkResult.isSuccess) {
                val activities = networkResult.getOrNull() ?: emptyList()
                updateLocalCache(activities)
                _state.value = activities
                Result.success(activities)
            } else {
                val localResult = local.readAll()
                if (localResult.isSuccess) {
                    val activities = localResult.getOrNull() ?: emptyList()
                    _state.value = activities
                    Result.success(activities)
                } else {
                    networkResult
                }
            }
        } catch (e: Exception) {
            val localResult = local.readAll()
            if (localResult.isSuccess) {
                val activities = localResult.getOrNull() ?: emptyList()
                _state.value = activities
                Result.success(activities)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getActivitiesByAdvenId(advenId: String): Result<List<Activity>> {
        return try {
            val networkResult = remote.getActivitiesByAdvenId(advenId)

            if (networkResult.isSuccess) {
                val activities = networkResult.getOrNull() ?: emptyList()
                updateLocalCacheForAdven(activities)
                Result.success(activities)
            } else {
                val localResult = local.readAll()
                if (localResult.isSuccess) {
                    val allActivities = localResult.getOrNull() ?: emptyList()
                    val filteredActivities = allActivities.filter { it.advenId == advenId }
                    Result.success(filteredActivities)
                } else {
                    networkResult
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOne(id: String): Result<Activity> {
        return try {
            val networkResult = remote.readOne(id)

            if (networkResult.isSuccess) {
                val activity = networkResult.getOrNull()
                activity?.let {
                    local.createOne(it)
                }
                networkResult
            } else {
                local.readOne(id)
            }
        } catch (e: Exception) {
            local.readOne(id)
        }
    }

    override suspend fun createActivity(
        title: String,
        img: Uri?,
        location: String,
        price: String,
        description: String,
        advenId: String?
    ): Result<Activity> {
        return try {
            val result = remote.createActivity(title, location, price, description, img, advenId)

            if (result.isSuccess) {
                val activity = result.getOrNull()
                activity?.let {
                    local.createOne(it)
                    val currentList = _state.value.toMutableList()
                    currentList.add(it)
                    _state.value = currentList
                }
            }
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateActivity(
        id: String,
        title: String,
        img: Uri?,
        location: String,
        price: String,
        description: String
    ): Activity {
        val response = remote.updateActivity(id, title, location, price, description, img)

        if (response.isSuccessful) {
            var updatedActivity = response.body()!!.data.toExternal()

            if (img != null) {
                val refreshResult = remote.readOne(id)
                if (refreshResult.isSuccess) {
                    updatedActivity = refreshResult.getOrNull()!!
                }
            }

            local.createOne(updatedActivity)

            val currentList = _state.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == id }
            if (index != -1) {
                currentList[index] = updatedActivity
                _state.value = currentList
            }

            return updatedActivity
        } else {
            throw Exception("Error al actualizar la actividad")
        }
    }

    override suspend fun deleteActivity(id: String): Result<Boolean> {
        return try {

            val result = remote.deleteActivity(id)

            if (result.isSuccess) {
                local.deleteActivity(id)
                Log.d("ROOM_DEBUG", "Room eliminó actividad")

                val currentList = _state.value.toMutableList()
                val activityToRemove = currentList.find { activity -> activity.id == id }
                if (activityToRemove != null) {
                    currentList.remove(activityToRemove)
                    _state.value = currentList
                }

                refreshCacheFromServer()
            }
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun setStream(): Flow<Result<List<Activity>>> {
        return local.observeAll()
            .onStart {
                try {
                    val networkResult = remote.getActivities()
                    if (networkResult.isSuccess) {
                        val activities = networkResult.getOrNull() ?: emptyList()
                        updateLocalCache(activities)
                    }
                } catch (e: Exception) {
                    // Ignore and use local cache
                }
            }
    }

    private suspend fun refreshCacheFromServer() {
        try {
            val networkResult = remote.getActivities()
            if (networkResult.isSuccess) {
                val activities = networkResult.getOrNull() ?: emptyList()

                local.clearAll()
                local.updateActivities(activities)
                _state.value = activities

            }
        } catch (e: Exception) {
        }
    }

    private suspend fun updateLocalCache(activities: List<Activity>) {
        try {
            local.updateActivities(activities)
        } catch (e: Exception) {
        }
    }

    private suspend fun updateLocalCacheForAdven(activities: List<Activity>) {
        try {
            activities.forEach { activity ->
                local.createOne(activity)
            }
        } catch (e: Exception) {
        }
    }
}