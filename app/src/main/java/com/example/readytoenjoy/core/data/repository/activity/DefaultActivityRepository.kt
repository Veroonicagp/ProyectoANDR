package com.example.readytoenjoy.core.data.repository.activity

import android.net.Uri
import android.util.Log
import com.example.readytoenjoy.core.data.network.activity.ActivityNetworkRepositoryInterface
import com.example.readytoenjoy.core.data.network.activity.model.toExternal
import com.example.readytoenjoy.core.data.network.adevn.model.toExternal
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.core.model.Adven
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class DefaultActivityRepository @Inject constructor(
    private val remote: ActivityNetworkRepositoryInterface
): ActivityRepositoryInterface {
    private val _state = MutableStateFlow<List<Activity>>(listOf())

    //obtiene la lista de actividades de todos los usuarios
    override suspend fun getActivities(): Result<List<Activity>> {
       val result = remote.getActivities()
        return result

    }

    //obtienes la lista de actividades del usuario conectado
    override suspend fun getActivitiesByAdvenId(advenId: String): Result<List<Activity>> {
        val response = remote.getActivitiesByAdvenId(advenId)

        return response
    }

    //obtiene uno
    override suspend fun getOne(id: String): Result<Activity> {
       return remote.readOne(id)
    }

    override suspend fun createActivity(
        title: String,
        img: Uri?,
        location: String,
        price: String,
        description: String,
        advenId: String?
    ): Result<Activity> {

        val result = remote.createActivity(title,location,price,description,img,advenId)
        if (result.isSuccess) {
            val activity = result.getOrNull()
            activity?.let {
                //local
            }
        }
        return result

    }

    override suspend fun updateActivity(
        id: String,
        title: String,
        img: Uri?,
        location: String,
        price: String,
        description: String
    ): Activity {
        val response = remote.updateActivity(id, title,location,price,description,img)
        if (response.isSuccessful) {
            var updatedActivity = response.body()!!.data.toExternal()
            if (img != null) {
                val refreshResult = remote.readOne(id)
                if (refreshResult.isSuccess) {
                    updatedActivity = refreshResult.getOrNull()!!
                }
            }
            val currentList = _state.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == id }
            if (index != -1) {
                currentList[index] = updatedActivity
                _state.value = currentList
            }
            return updatedActivity
        } else {
            throw Exception("Error al actualizar el aventurero")
        }
    }

    override suspend fun deleteActivity(id: String): Result<Boolean> {
        val result = remote.deleteActivity(id)

        if (result.isSuccess) {
            val currentList = _state.value.toMutableList()
            val activityToRemove = currentList.find { activity -> activity.id == id }

            if (activityToRemove != null) {
                currentList.remove(activityToRemove)
                _state.value = currentList
            }
        }

        return result
    }

    override fun setStream(): Flow<Result<List<Activity>>> {
        val activities = flow<Result<List<Activity>>> {
            val result = remote.getActivities()
            emit(result)
        }
        return activities
    }

}