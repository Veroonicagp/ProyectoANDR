package com.example.readytoenjoy.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.data.repository.adven.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginUiState {
    object Idle: LoginUiState()
    object Loading: LoginUiState()
    object Success: LoginUiState()
    class Error(val message: String): LoginUiState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState>
        get() = _uiState.asStateFlow()

    fun login(identifier: String, password: String) {
        viewModelScope.launch {
            try {
                val jwt = repository.login(identifier, password)

                if (jwt.isNullOrEmpty()) {
                    _uiState.value = LoginUiState.Error("Usuario o contraseña incorrectos")
                } else {
                    _uiState.value = LoginUiState.Success
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("Error de conexión")
            }
        }
    }
}