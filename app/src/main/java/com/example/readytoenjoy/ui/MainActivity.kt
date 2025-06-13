/**
 * @file MainActivity.kt
 * @brief Activity principal de la aplicación con navegación bottom
 * @details Contiene el NavHostFragment y maneja la navegación principal de la app
 * @author ReadyToEnjoy Team
 * @version 1.0
 */

package com.example.readytoenjoy.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.setupWithNavController
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import com.example.readytoenjoy.R
import com.example.readytoenjoy.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @class MainActivity
 * @brief Activity principal que gestiona la navegación de la aplicación
 * @details Contiene el NavHostFragment principal y la bottom navigation bar.
 *          Maneja la visibilidad de la barra de navegación según el destino actual.
 * @activity
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    /** @brief Binding para el layout de la activity principal */
    private lateinit var binding: ActivityMainBinding

    /**
     * @brief Inicializa la activity y configura la navegación
     * @param savedInstanceState Estado guardado anterior
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
    }

    /**
     * @brief Configura el sistema de navegación principal
     * @details Establece la conexión entre el NavController y la bottom navigation,
     *          configura listeners para controlar la visibilidad de la barra
     */
    private fun setupNavigation() {
        val navigationBar = binding.bottomNavigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navigation_container) as NavHostFragment
        val navController = navHostFragment.navController

        // Conectar bottom navigation con NavController
        navigationBar.setupWithNavController(navController)

        // Listener para controlar visibilidad de la barra de navegación
        navController.addOnDestinationChangedListener { _, _, args ->
            val showNavbar = args?.getBoolean("showNavbar", true) ?: true
            binding.bottomNavigation.isVisible = showNavbar
        }
    }
}