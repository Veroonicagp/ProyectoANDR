package com.example.readytoenjoy.ui.adven.activitiesAdvenList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.data.repository.activity.ActivityRepositoryInterface
import com.example.readytoenjoy.core.model.Activity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AdvenActListViewModel @Inject constructor (private val defaultMyActivityRepository: ActivityRepositoryInterface,):
    ViewModel() {

    private val _uiState = MutableStateFlow<ActivitiesListUiState>(ActivitiesListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun load(advenId: String) {
        if (advenId == null) return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val activities = defaultMyActivityRepository.getActivitiesByAdvenId(advenId)
                activities
                if (activities == null) {
                    _uiState.value = ActivitiesListUiState.Loading
                } else {
                    _uiState.value = ActivitiesListUiState.Success(activities.getOrNull()!!)
                }
            }
        }
    }
    sealed class ActivitiesListUiState() {
        data object Loading: ActivitiesListUiState()
        class Success(val ActivitiesList: List<Activity>): ActivitiesListUiState()
        class Error(val message: String): ActivitiesListUiState()
    }
}