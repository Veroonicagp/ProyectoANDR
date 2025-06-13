/**
 * @file SplashActivity.kt
 * @brief Activity de splash screen con manejo de conectividad
 * @details Pantalla inicial que verifica el estado de login y conectividad antes de dirigir al usuario
 * @author ReadyToEnjoy Team
 * @version 1.0
 */

package com.example.readytoenjoy.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.readytoenjoy.R
import com.example.readytoenjoy.core.utils.ConnectivityHelper
import com.example.readytoenjoy.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @class SplashActivity
 * @brief Activity inicial de la aplicación con lógica de enrutamiento
 * @details Maneja la pantalla de splash con verificación de conectividad y estado de autenticación.
 *          Implementa estrategia offline-first para usuarios que ya tienen datos locales.
 * @activity
 */
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    /** @brief ViewModel para operaciones de verificación de estado */
    private val viewModel: SplashViewModel by viewModels()

    /** @brief Helper para verificar conectividad de red @inject */
    @Inject
    lateinit var connectivityHelper: ConnectivityHelper

    /**
     * @brief Inicializa la activity y arranca el proceso de verificación
     * @param savedInstanceState Estado guardado anterior
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startApp()
    }

    /**
     * @brief Inicia el proceso de verificación con delay de splash
     * @details Muestra splash por 2.5 segundos luego verifica conectividad y estado de login
     */
    private fun startApp() {
        lifecycleScope.launch {
            delay(2500) // Tiempo de splash screen

            if (connectivityHelper.isNetworkAvailable()) {
                checkLoginOnline()
            } else {
                handleOfflineStart()
            }
        }
    }

    /**
     * @brief Verifica el estado de login cuando hay conectividad
     * @details Si el usuario tiene sesión válida va a MainActivity, sino a LoginActivity
     */
    private suspend fun checkLoginOnline() {
        val isLoggedIn = viewModel.isUserLoggedIn()

        if (isLoggedIn) {
            navigateToMain()
        } else {
            navigateToLogin()
        }
    }

    /**
     * @brief Maneja el inicio cuando no hay conectividad
     * @details Verifica si hay datos locales para permitir uso offline
     */
    private suspend fun handleOfflineStart() {
        val hasLocalData = viewModel.hasLocalUserData()

        if (hasLocalData) {
            showOfflineToast()
            navigateToMain()
        } else {
            showNeedConnectionToast()
            navigateToLogin()
        }
    }

    /**
     * @brief Muestra mensaje informativo sobre uso offline
     */
    private fun showOfflineToast() {
        Toast.makeText(
            this,
            "Sin conexión. Usando datos guardados.",
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * @brief Muestra mensaje indicando que se necesita conexión
     */
    private fun showNeedConnectionToast() {
        Toast.makeText(
            this,
            "Se requiere conexión para el primer inicio.",
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * @brief Navega a la actividad principal
     * @details Cierra splash y abre MainActivity
     */
    private fun navigateToMain() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }

    /**
     * @brief Navega a la actividad de login
     * @details Cierra splash y abre LoginActivity
     */
    private fun navigateToLogin() {
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        finish()
    }
}