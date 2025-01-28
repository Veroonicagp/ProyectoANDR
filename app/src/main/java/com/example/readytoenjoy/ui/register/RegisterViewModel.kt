package com.example.readytoenjoy.ui.register


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.data.adven.RegisterRepository
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
class RegisterViewModel @Inject constructor(private val repository: RegisterRepository):ViewModel()     {

    private val _user = MutableStateFlow<UiState>(UiState.Loading)
    val user: StateFlow<UiState>
        get() = _user.asStateFlow()

    fun register(name:String, surname:String, email:String, password:String) {
        viewModelScope.launch {
            _user.value = if (repository.register(name,surname,password,email)) {
                UiState.Success
            }else {
                UiState.Error("El usuario ya existe")
            }
        }
    }
}