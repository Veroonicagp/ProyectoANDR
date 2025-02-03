package com.example.readytoenjoy.core.data.adven

import com.example.readytoenjoy.core.network.adevn.AdvenNetworkRepositoryInterface
import com.example.readytoenjoy.core.network.adevn.AdvenResponseLR
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
fun AdvenResponseLR.toExternal(): Adven {
    return Adven(
        id = this.id,
        name = this.attributes.name,
        surname =  this.attributes.surname,
        email = this.attributes.email
    )
}

fun List<AdvenResponseLR>.toExternal():List<Adven> {
    return this.map(AdvenResponseLR::toExternal)
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
        val response = advenNetworkRepository.readOneAdven(id)
        return if (response.isSuccessful) response.body()!!
        else Adven("", "","","")
    }

    override val setStream: StateFlow<List<Adven>>
            get() = _state.asStateFlow()

}