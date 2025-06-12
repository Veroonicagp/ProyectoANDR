package com.example.readytoenjoy.ui.porfile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _photo = MutableStateFlow<Uri>(Uri.EMPTY)
    val photo: StateFlow<Uri> = _photo.asStateFlow()

    init {
        loadInitialProfile()
    }

    private fun loadInitialProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading

            val advenId = loginRepository.getAdvenId()
            if (advenId != null) {
                loadProfileData(advenId)
            } else {
                _uiState.value = ProfileUiState.Error("No se encontró el ID del aventurero")
            }
        }
    }

    private suspend fun loadProfileData(advenId: String) {
        try {
            val adven = advenRepository.getOne(advenId)
            _uiState.value = ProfileUiState.Wait(adven)
        } catch (e: Exception) {
            _uiState.value = ProfileUiState.Error("Error al cargar el perfil: ${e.message}")
        }
    }

    fun updateProfile(name: String, media: Uri?, email: String) {
        viewModelScope.launch {
            val advenId = loginRepository.getAdvenId()

            if (advenId != null) {
                performProfileUpdate(advenId, name, media, email)
            } else {
                _uiState.value = ProfileUiState.Error("No se encontró el ID del aventurero")
            }
        }
    }

    private suspend fun performProfileUpdate(advenId: String, name: String, media: Uri?, email: String) {
        try {
            _uiState.value = ProfileUiState.Loading
            val updatedAdven = advenRepository.updateAdven(advenId, media, name, email)
            _uiState.value = ProfileUiState.Success(updatedAdven)
            _uiState.value = ProfileUiState.Wait(updatedAdven)
        } catch (e: Exception) {
            _uiState.value = ProfileUiState.Error("Error al actualizar: ${e.message}")
        }
    }

    fun onImageCaptured(uri: Uri?) {
        viewModelScope.launch {
            uri?.let { capturedUri ->
                resetPhotoState()
                _photo.value = capturedUri
            }
        }
    }

    private fun resetPhotoState() {
        _photo.value = Uri.EMPTY
    }
}

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Wait(val adven: Adven) : ProfileUiState()
    data class Success(val adven: Adven) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}