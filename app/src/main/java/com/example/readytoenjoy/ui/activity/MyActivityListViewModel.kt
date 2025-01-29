package com.example.readytoenjoy.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.data.activity.Activity
import com.example.readytoenjoy.core.data.activity.ActivityRepositoryInterface
import com.example.readytoenjoy.core.data.adven.RegisterRepository
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
    private val defaultMyActivityRepository: ActivityRepositoryInterface
):ViewModel() {

    private val _uiState = MutableStateFlow<MyActivityListUiState>(MyActivityListUiState.Loading)
    val uiState: StateFlow<MyActivityListUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                defaultMyActivityRepository.setStream.collect {
                        activityList ->
                    if (activityList.isEmpty()) _uiState.value = MyActivityListUiState.Loading
                    else _uiState.value = MyActivityListUiState.Success(activityList)
                }
            }
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                //TODO tengo que recoger el advenId del adven asociado al user
                //defaultMyActivityRepository.getActivitiesByAdvenId(advenId)
            }
        }

    }
}

sealed class MyActivityListUiState() {
    data object Loading: MyActivityListUiState()
    class Success(val myActivityList: List<Activity>): MyActivityListUiState()
    class Error(val message: String): MyActivityListUiState()
}