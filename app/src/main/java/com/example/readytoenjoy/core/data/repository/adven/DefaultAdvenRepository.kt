package com.example.readytoenjoy.core.data.repository.adven

import com.example.readytoenjoy.core.data.network.adevn.AdvenNetworkRepositoryInterface
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenResponseLR
import com.example.readytoenjoy.core.data.network.adevn.model.toExternal
import com.example.readytoenjoy.core.model.Adven
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DefaultAdvenRepository @Inject constructor(private val advenNetworkRepository: AdvenNetworkRepositoryInterface,):
    AdvenRepositoryInterface {

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
        return if (response.isSuccessful) {
            response.body()!!.data.toExternal()
        } else {
            Adven("", "", "",null)
        }
    }

    override suspend fun updateAdven(id: String, name: String, email: String): Adven {
        val response = advenNetworkRepository.updateAdven(id, name, email)
        if (response.isSuccessful) {
            val updatedAdven = response.body()!!.data.toExternal()
            // Actualizar el estado
            val currentList = _state.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == id }
            if (index != -1) {
                currentList[index] = updatedAdven
                _state.value = currentList
            }
            return updatedAdven
        } else {
            throw Exception("Error al actualizar el aventurero")
        }
    }

    override val setStream: StateFlow<List<Adven>>
            get() = _state.asStateFlow()

}