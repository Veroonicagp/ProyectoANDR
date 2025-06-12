package com.example.readytoenjoy.ui.activity.create

import android.annotation.SuppressLint
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.data.repository.activity.ActivityRepositoryInterface
import com.example.readytoenjoy.core.data.repository.adven.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiState {
    object Loading: UiState()
    object Ready: UiState()
    class Created(val id: String): UiState()
    class Error(val message: String): UiState()
}

@HiltViewModel
class CreateActivityViewModel @Inject constructor(
    private val repository: ActivityRepositoryInterface,
    private val loginRepository: LoginRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    init {
        resetState()
    }

    fun resetState() {
        _uiState.value = UiState.Ready
    }

    private val _photo = MutableStateFlow<Uri>(Uri.EMPTY)
    val photo: StateFlow<Uri>
        get() = _photo.asStateFlow()

    fun onImageCaptured(uri: Uri?) {
        viewModelScope.launch {
            uri?.let {
                _photo.value = uri
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun create(title: String, img: Uri?, location: String, price: String, description: String, advenId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val actualAdvenId = loginRepository.getAdvenId()

                if (actualAdvenId != null) {
                    val result = repository.createActivity(
                        title,
                        img,
                        location,
                        price,
                        description,
                        advenId = actualAdvenId
                    )

                    handleCreateResult(result)
                } else {
                    _uiState.value = UiState.Error("No se encontró el ID del aventurero")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error inesperado: ${e.message}")
            }
        }
    }

    private fun handleCreateResult(result: Result<com.example.readytoenjoy.core.model.Activity>) {
        if (result.isSuccess) {
            val activityId = result.getOrNull()?.id
            if (activityId != null) {
                _uiState.value = UiState.Created(activityId)
            } else {
                _uiState.value = UiState.Error("Error al obtener el ID de la actividad creada")
            }
        } else {
            val error = result.exceptionOrNull()?.message ?: "Error desconocido"
            _uiState.value = UiState.Error("Error al crear la actividad: $error")
        }
    }
}