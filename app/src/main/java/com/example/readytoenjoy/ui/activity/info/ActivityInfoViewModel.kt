package com.example.readytoenjoy.ui.activity.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.data.repository.activity.ActivityRepositoryInterface
import com.example.readytoenjoy.core.data.repository.adven.LoginRepository
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.ui.activity.edit.EditActivityUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityInfoViewModel @Inject constructor (private val repository: ActivityRepositoryInterface, private val loginRepository: LoginRepository):
    ViewModel() {

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


}

sealed class InfoActivityUiState {
    object Loading : InfoActivityUiState()
    data class Success(val activity: Activity) : InfoActivityUiState()
    data class Error(val message: String) : InfoActivityUiState()
}