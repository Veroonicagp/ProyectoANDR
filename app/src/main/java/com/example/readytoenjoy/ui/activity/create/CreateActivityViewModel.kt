package com.example.readytoenjoy.ui.activity.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.data.activity.DefaultActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiState {

    object Started: UiState()
    object Loading: UiState()
    object Success:UiState()
    class Error(val message: String): UiState()
}

@HiltViewModel
class CreateActivityViewModel @Inject constructor(private val repository: DefaultActivityRepository):ViewModel() {

    private val _activity = MutableStateFlow<UiState>(UiState.Loading)
    val activity: StateFlow<UiState>
        get()= _activity.asStateFlow()

    fun create( title: String, location:String, price:String, description: String, advenId: String?){
        viewModelScope.launch {
            val id = repository
        }
    }


}