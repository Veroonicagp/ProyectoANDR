package com.example.readytoenjoy.ui.myActivities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.core.data.repository.activity.ActivityRepositoryInterface
import com.example.readytoenjoy.core.data.repository.adven.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyActivityListViewModel @Inject constructor(
    private val defaultMyActivityRepository: ActivityRepositoryInterface,
    private val loginRepository: LoginRepository
):ViewModel() {

    private val _uiState = MutableStateFlow<MyActivityListUiState>(MyActivityListUiState.Loading)
    val uiState: StateFlow<MyActivityListUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Obtener el advenId desde DataStore
            val advenId = loginRepository.getAdvenId()
            if (!advenId.isNullOrEmpty()) {
                // Usar advenId para obtener actividades
                //loadActivities(advenId)
            } else {
                _uiState.value = MyActivityListUiState.Error("No se encontró el ID del aventurero.")
            }
        }

    }
    /**private fun loadActivities(advenId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val activities = defaultMyActivityRepository.getActivitiesByAdvenId(advenId)
                if (activities.isEmpty()) {
                    _uiState.value = MyActivityListUiState.Loading
                } else {
                    _uiState.value = MyActivityListUiState.Success(activities)
                }
            }
        }
    }**/

}


sealed class MyActivityListUiState() {
    data object Loading: MyActivityListUiState()
    class Success(val myActivityList: List<Activity>): MyActivityListUiState()
    class Error(val message: String): MyActivityListUiState()
}