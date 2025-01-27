package com.example.readytoenjoy.core.data.adven

import com.example.readytoenjoy.core.network.adevn.AdvenNetworkRepositoryInterface
import com.example.readytoenjoy.core.network.adevn.AdvenResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
fun AdvenResponse.toExternal(): Adven {
    return Adven(
        id = this.id,
        name = this.attributes.name,
        surname =  this.attributes.surname,
        email = this.attributes.email,
        userId = this.attributes.userId
    )
}

fun List<AdvenResponse>.toExternal():List<Adven> {
    return this.map(AdvenResponse::toExternal)
}

@Singleton
class DefaultAdvenRepository @Inject constructor(private val advenNetworkRepository: AdvenNetworkRepositoryInterface,):AdvenRepositoryInterface {

    //preguntar uso
    private val _state = MutableStateFlow<List<Adven>>(listOf())
    override suspend fun getAdvens(): List<Adven> {
        val response = advenNetworkRepository.readAdven()
        return if(response.isSuccessful){
            val advens = response.body()!!.data.toExternal()
            _state.value = advens
            advens
        }else{
            _state.value = listOf()
            listOf()
        }
    }

    override suspend fun getOne(id: String): Adven {
        TODO("Not yet implemented")
    }

    override val setStream: StateFlow<List<Adven>>
            get() = _state.asStateFlow()

}