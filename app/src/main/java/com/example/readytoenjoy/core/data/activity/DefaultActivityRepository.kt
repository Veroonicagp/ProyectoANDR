package com.example.readytoenjoy.core.data.activity

import com.example.readytoenjoy.core.network.activity.ActivityNetworkRepositoryInterface
import com.example.readytoenjoy.core.network.activity.ActivityResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

fun ActivityResponse.toExternal(): Activity {
    return Activity(
        id = this.id,
        title = this.attributes.title,
        location =  this.attributes.location,
        price = this.attributes.price,
        description = this.attributes.description
    )
}
fun List<ActivityResponse>.toExternal():List<Activity> {
    return this.map(ActivityResponse::toExternal)
}

@Singleton
class DefaultActivityRepository @Inject constructor(
    private val activityNetworkRepository: ActivityNetworkRepositoryInterface
): ActivityRepositoryInterface {
    private val _state = MutableStateFlow<List<Activity>>(listOf())
    override val setStream: StateFlow<List<Activity>>
        get() = _state.asStateFlow()

    //obtiene la lista de actividades de todos los usuarios
    override suspend fun getActivities(): List<Activity> {
       val response = activityNetworkRepository.getActivities()
        //val activityList = _state.value.toMutableList()
        return if (response.isSuccessful){
            val activities = response.body()!!.data.toExternal()
            _state.value = activities
            activities
        } else {
            _state.value = listOf()
            listOf()
        }
    }

    //obtienes la lista de actividades del usuario conectado
    override suspend fun getActivitiesByAdvenId(advenId: String): List<Activity> {
        val response = activityNetworkRepository.getActivitiesByAdvenId(advenId)
        return if (response.isSuccessful){
            val activities = response.body()!!.data.toExternal()
            _state.value = activities
            activities
        } else {
            _state.value = listOf()
            listOf()
        }
    }

    //obtiene uno
    override suspend fun getOne(id: String): Activity {
        val response = activityNetworkRepository.readOne(id)
        return if (response.isSuccessful) response.body()!!
        else Activity("", "","","","")
    }



}