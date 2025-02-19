package com.example.readytoenjoy.ui.porfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.data.network.adevn.AdvenNetworkRepository
import com.example.readytoenjoy.core.data.repository.adven.AdvenRepositoryInterface
import com.example.readytoenjoy.core.data.repository.adven.LoginRepository
import com.example.readytoenjoy.core.model.Adven
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val advenRepository: AdvenRepositoryInterface,
    private val loginRepository: LoginRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                val advenId = loginRepository.getAdvenId()
                if (advenId != null) {
                    val adven = advenRepository.getOne(advenId)
                    _uiState.value = ProfileUiState.Wait(adven)
                } else {
                    _uiState.value = ProfileUiState.Error("No se encontró el ID del aventurero")
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Error desconocido")
            }
        }

    }

    fun updateProfile(name: String, email: String) {
        viewModelScope.launch {
            try {
                val advenId = loginRepository.getAdvenId()
                if (advenId != null) {
                    _uiState.value = ProfileUiState.Loading
                    val updatedAdven = advenRepository.updateAdven(advenId, name, email)
                    _uiState.value = ProfileUiState.Success(updatedAdven)
                    _uiState.value = ProfileUiState.Wait(updatedAdven)
                } else {
                    _uiState.value = ProfileUiState.Error("No se encontró el ID del aventurero")
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Error al actualizar")
            }
        }
    }
}

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Wait(val adven: Adven) : ProfileUiState()
    data class Success(val adven: Adven) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}