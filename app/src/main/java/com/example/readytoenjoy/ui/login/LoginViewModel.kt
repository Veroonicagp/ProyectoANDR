package com.example.readytoenjoy.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.data.repository.adven.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.Identifier
import javax.inject.Inject

sealed class LoginUiState {
    object Loading: LoginUiState()
    object Success:LoginUiState()
    class Error(val message: String): LoginUiState()
}
@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository): ViewModel(){

    private val _user = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val uiState: StateFlow<LoginUiState>
        get() = _user.asStateFlow()



    fun login(identifier:String, password:String) {
        viewModelScope.launch {
            _user.value = LoginUiState.Loading
            val jwt = repository.login(identifier,password)
            if (jwt == null) {
                _user.value = LoginUiState.Error("Mala contraseña o usuario")
            }
            else {
                _user.value = LoginUiState.Success
            }

        }
    }

    fun loginAdven(email:String, password:String) {
        viewModelScope.launch {
            _user.value = LoginUiState.Loading
            val jwt = repository.login(email,password)
            if (jwt == null) {
                _user.value = LoginUiState.Error("Mala contraseña o usuario")
            }
            else {
                _user.value = LoginUiState.Success
            }

        }
    }
}