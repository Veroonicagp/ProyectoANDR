package com.example.readytoenjoy.ui.activity.edit

import android.icu.text.CaseMap.Title
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.data.repository.activity.ActivityRepositoryInterface
import com.example.readytoenjoy.core.data.repository.adven.LoginRepository
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.ui.porfile.ProfileUiState
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
        if (activityId == null) return

        viewModelScope.launch {
            _uiState.value = EditActivityUiState.Loading
            try {
                val result = repository.getOne(activityId)
                if (result.isSuccess) {
                    val activity = result.getOrNull()
                    if (activity != null) {
                        _uiState.value = EditActivityUiState.Success(activity)
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

    fun updateActivity(activityId: String,title: String, price: String, location:String, description:String) {
        viewModelScope.launch {
            try {
                if (activityId != null) {
                    _uiState.value = EditActivityUiState.Loading
                    val updatedActivity = repository.updateActivity(activityId, title,location,price,description)
                    _uiState.value = EditActivityUiState.Success(updatedActivity)
                    _uiState.value = EditActivityUiState.Wait(updatedActivity)
                } else {
                    _uiState.value = EditActivityUiState.Error("No se encontró el ID de la actividad")
                }
            } catch (e: Exception) {
                _uiState.value = EditActivityUiState.Error(e.message ?: "Error al actualizar")
            }
        }
    }

}

sealed class EditActivityUiState {
    object Loading : EditActivityUiState()
    data class Wait(val activity: Activity) : EditActivityUiState()
    data class Success(val activity: Activity) : EditActivityUiState()
    data class Error(val message: String) : EditActivityUiState()
}