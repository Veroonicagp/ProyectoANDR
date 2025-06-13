/**
 * @file RegisterViewModel.kt
 * @brief ViewModel para la gestión del registro de usuarios
 * @details Maneja la lógica de negocio para el registro de nuevos usuarios
 * @author ReadyToEnjoy Team
 * @version 1.0
 */

package com.example.readytoenjoy.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.data.repository.adven.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @enum UiState
 * @brief Estados de la interfaz de usuario para el proceso de registro
 * @details Define los diferentes estados que puede tener la UI durante el registro
 */
sealed class UiState {
    /** @brief Estado inicial del registro */
    object Started: UiState()

    /** @brief Estado de carga, registro en progreso */
    object Loading: UiState()

    /** @brief Estado de éxito, registro completado */
    object Success: UiState()

    /** @brief Estado de error con mensaje descriptivo */
    class Error(val message: String): UiState()
}

/**
 * @class RegisterViewModel
 * @brief ViewModel que gestiona el proceso de registro de usuarios
 * @details Coordina la comunicación con el repositorio de registro y maneja
 *          los estados de la UI durante el proceso
 * @viewmodel
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: RegisterRepository
): ViewModel() {

    /** @brief Estado de la UI para el proceso de registro */
    private val _user = MutableStateFlow<UiState>(UiState.Started)
    val user: StateFlow<UiState>
        get() = _user.asStateFlow()

    /**
     * @brief Registra un nuevo usuario en el sistema
     * @details Envía los datos del usuario al repositorio y actualiza el estado según el resultado
     * @param username Nombre de usuario único
     * @param email Dirección de correo electrónico
     * @param password Contraseña del usuario
     */
    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _user.value = UiState.Loading

            try {
                val jwt = repository.register(username, email, password)

                if (jwt == null) {
                    _user.value = UiState.Error("El usuario ya existe")
                } else {
                    _user.value = UiState.Success
                }
            } catch (e: Exception) {
                _user.value = UiState.Error("Error en el registro: ${e.message}")
            }
        }
    }
}