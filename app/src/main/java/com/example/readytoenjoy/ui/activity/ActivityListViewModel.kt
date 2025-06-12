package com.example.readytoenjoy.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.core.data.repository.activity.ActivityRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ActivityListViewModel @Inject constructor(
    private val defaultActivityRepository: ActivityRepositoryInterface
): ViewModel() {

    private val _uiState = MutableStateFlow<ActivityListUiState>(ActivityListUiState.Loading)
    val uiState: StateFlow<ActivityListUiState>
        get() = _uiState.asStateFlow()

    init {
        loadActivities()
    }

    fun loadActivities() {
        viewModelScope.launch {
            try {
                _uiState.value = ActivityListUiState.Loading

                withContext(Dispatchers.Main) {
                    defaultActivityRepository.setStream().collect { result ->
                        if (result.isSuccess) {
                            val activities = result.getOrNull() ?: emptyList()
                            _uiState.value = ActivityListUiState.Success(activities)
                        } else {
                            _uiState.value = ActivityListUiState.Error(
                                result.exceptionOrNull()?.message ?: "Error al recuperar actividades"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ActivityListUiState.Error("Error inesperado: ${e.message}")
            }
        }
    }

}

sealed class ActivityListUiState {
    data object Loading: ActivityListUiState()
    class Success(val activityList: List<Activity>): ActivityListUiState()
    class Error(val message: String): ActivityListUiState()
}