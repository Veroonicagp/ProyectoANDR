package com.example.readytoenjoy.ui.myActivities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.core.data.repository.activity.ActivityRepositoryInterface
import com.example.readytoenjoy.core.data.repository.adven.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyActivityListViewModel @Inject constructor(
    private val defaultMyActivityRepository: ActivityRepositoryInterface,
    private val loginRepository: LoginRepository
):ViewModel() {

    private val _uiState = MutableStateFlow<MyActivityListUiState>(MyActivityListUiState.Loading)
    val uiState: StateFlow<MyActivityListUiState>
        get() = _uiState.asStateFlow()

    private val _deleteState = MutableStateFlow<DeleteActivityState>(DeleteActivityState.Loading)
    val deleteState = _deleteState.asStateFlow()

    init {
     load()
    }

    fun load(){
        viewModelScope.launch {
            val advenId = loginRepository.getAdvenId()
            if (!advenId.isNullOrEmpty()) {
                loadActivities(advenId)
            } else {
                _uiState.value = MyActivityListUiState.Error("No se encontró el ID del aventurero.")
            }
        }
    }
    private fun loadActivities(advenId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val activities = defaultMyActivityRepository.getActivitiesByAdvenId(advenId)
                activities
                if (activities == null) {
                    _uiState.value = MyActivityListUiState.Loading
                } else {
                    _uiState.value = MyActivityListUiState.Success(activities.getOrNull()!!)
                }
            }
        }
    }
    fun deleteActivity(activity: Activity) {
        viewModelScope.launch {
            _deleteState.value = DeleteActivityState.Loading
            withContext(Dispatchers.IO) {
                val result = defaultMyActivityRepository.deleteActivity(activity.id)

                if (result.isSuccess) {
                    _deleteState.value = DeleteActivityState.DeleteSuccess
                    load()
                } else {
                    _deleteState.value = DeleteActivityState.DeleteError(
                        result.exceptionOrNull()?.message ?: "Error al eliminar la actividad"
                    )
                }
            }
        }
    }

    fun resetDeleteState() {
        _deleteState.value = DeleteActivityState.Loading
    }

}


sealed class MyActivityListUiState() {
    data object Loading: MyActivityListUiState()
    class Success(val myActivityList: List<Activity>): MyActivityListUiState()
    class Error(val message: String): MyActivityListUiState()
}

sealed class DeleteActivityState {
    data object Loading: DeleteActivityState()
    data object DeleteSuccess: DeleteActivityState()
    class DeleteError(val message: String): DeleteActivityState()
}