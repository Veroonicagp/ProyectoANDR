/**
 * @file SplashViewModel.kt
 * @brief ViewModel para la lógica de verificación del SplashScreen
 * @details Gestiona la verificación de estado de autenticación y datos locales
 * @author ReadyToEnjoy Team
 * @version 1.0
 */

package com.example.readytoenjoy.ui

import androidx.lifecycle.ViewModel
import com.example.readytoenjoy.core.data.repository.adven.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @class SplashViewModel
 * @brief ViewModel que gestiona la lógica de verificación inicial de la aplicación
 * @details Verifica el estado de autenticación del usuario y la disponibilidad de datos locales
 *          para determinar la ruta de navegación apropiada
 * @viewmodel
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    /**
     * @brief Verifica si el usuario tiene una sesión válida
     * @details Comprueba la existencia de token JWT y ID de aventurero válidos
     * @return Boolean true si el usuario está autenticado, false en caso contrario
     * @throws Exception Si hay error al acceder al almacenamiento local
     */
    suspend fun isUserLoggedIn(): Boolean {
        return try {
            val jwt = loginRepository.getToken()
            val advenId = loginRepository.getAdvenId()
            !jwt.isNullOrEmpty() && !advenId.isNullOrEmpty()
        } catch (e: Exception) {
            false
        }
    }

    /**
     * @brief Verifica si existen datos de usuario almacenados localmente
     * @details Comprueba si hay un ID de aventurero guardado para uso offline
     * @return Boolean true si hay datos locales disponibles, false en caso contrario
     * @throws Exception Si hay error al acceder al almacenamiento local
     */
    suspend fun hasLocalUserData(): Boolean {
        return try {
            val advenId = loginRepository.getAdvenId()
            !advenId.isNullOrEmpty()
        } catch (e: Exception) {
            false
        }
    }
}