package com.example.readytoenjoy.core.data.repository.activity

import com.example.readytoenjoy.core.data.network.activity.ActivityNetworkRepositoryInterface
import com.example.readytoenjoy.core.model.Activity
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


    /**override val setStream: StateFlow<List<Activity>>
        get() = _state.asStateFlow()**/

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
        //img: Uri?,
        location: String,
        price: String,
        description: String,
        //advenId: String?
    ): Result<Activity> {

        val result = remote.createActivity(title,location,price,description)
        if (result.isSuccess) {
            val activity = result.getOrNull()
            activity?.let {
                //local
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