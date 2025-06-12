package com.example.readytoenjoy.core.data.repository.adven

import android.net.Uri
import com.example.readytoenjoy.core.data.network.adevn.AdvenNetworkRepositoryInterface
import com.example.readytoenjoy.core.data.network.adevn.model.toExternal
import com.example.readytoenjoy.core.model.Adven
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultAdvenRepository @Inject constructor(
    private val advenNetworkRepository: AdvenNetworkRepositoryInterface,
): AdvenRepositoryInterface {

    private val _state = MutableStateFlow<List<Adven>>(listOf())

    override suspend fun getAdvens(): List<Adven> {
        return try {
            val response = advenNetworkRepository.readAdven()
            if (response.isSuccessful && response.body() != null) {
                val advens = response.body()!!.data.toExternal()
                _state.value = advens
                advens
            } else {
                // En caso de error, devolver lista vacía pero mantener estado anterior
                _state.value.ifEmpty { emptyList() }
            }
        } catch (e: Exception) {
            // En caso de excepción, devolver estado anterior o lista vacía
            _state.value.ifEmpty { emptyList() }
        }
    }

    override suspend fun getOne(id: String): Adven {
        return try {
            val response = advenNetworkRepository.readOneAdven(id)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.data.toExternal()
            } else {
                Adven("", "", "", null)
            }
        } catch (e: Exception) {
            Adven("", "", "", null)
        }
    }

    override suspend fun updateAdven(id: String, media: Uri?, name: String, email: String): Adven {
        val response = advenNetworkRepository.updateAdven(id, media, name, email)
        if (response.isSuccessful && response.body() != null) {
            val updatedAdven = response.body()!!.data.toExternal()

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