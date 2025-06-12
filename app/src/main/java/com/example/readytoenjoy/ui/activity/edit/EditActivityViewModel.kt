package com.example.readytoenjoy.ui.activity.edit

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.data.repository.activity.ActivityRepositoryInterface
import com.example.readytoenjoy.core.model.Activity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditActivityViewModel @Inject constructor(
    private val repository: ActivityRepositoryInterface
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditActivityUiState>(EditActivityUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadActivity(activityId: String?) {
        if (activityId == null) {
            return
        }

        viewModelScope.launch {
            _uiState.value = EditActivityUiState.Loading

            try {
                val result = repository.getOne(activityId)

                if (result.isSuccess) {
                    val activity = result.getOrNull()
                    if (activity != null) {
                        _uiState.value = EditActivityUiState.ActivityLoaded(activity)
                    } else {
                        _uiState.value = EditActivityUiState.Error("No se encontró la actividad")
                    }
                } else {
                    _uiState.value = EditActivityUiState.Error("Error al cargar la actividad")
                }
            } catch (e: Exception) {
                _uiState.value = EditActivityUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun updateActivity(activityId: String, title: String, img: Uri?, price: String, location: String, description: String) {
        viewModelScope.launch {
            try {
                _uiState.value = EditActivityUiState.Loading
                val updatedActivity = repository.updateActivity(activityId, title, img, location, price, description)
                _uiState.value = EditActivityUiState.Success(updatedActivity)
            } catch (e: Exception) {
                _uiState.value = EditActivityUiState.Error(e.message ?: "Error al actualizar")
            }
        }
    }
}

sealed class EditActivityUiState {
    object Loading : EditActivityUiState()
    data class ActivityLoaded(val activity: Activity) : EditActivityUiState()
    data class Wait(val activity: Activity) : EditActivityUiState()
    data class Success(val activity: Activity) : EditActivityUiState()
    data class Error(val message: String) : EditActivityUiState()
}