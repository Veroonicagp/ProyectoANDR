/**
 * @file RegisterActivity.kt
 * @brief Activity para el registro de nuevos usuarios
 * @details Maneja el formulario de registro con validaciones y verificación de conectividad
 * @author ReadyToEnjoy Team
 * @version 1.0
 */

package com.example.readytoenjoy.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.readytoenjoy.core.utils.ConnectivityHelper
import com.example.readytoenjoy.databinding.ActivityRegisterBinding
import com.example.readytoenjoy.ui.login.LoginActivity
import com.example.readytoenjoy.ui.utils.OfflineUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @class RegisterActivity
 * @brief Activity para el registro de nuevos usuarios en la aplicación
 * @details Proporciona un formulario de registro con validaciones del lado cliente,
 *          verificación de conectividad y manejo de estados de UI
 * @activity
 */
@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    /** @brief ViewModel para operaciones de registro @viewmodel */
    private val vm: RegisterViewModel by viewModels()

    /** @brief Binding para el layout de registro */
    private lateinit var binding: ActivityRegisterBinding

    /** @brief Helper para verificar conectividad @inject */
    @Inject
    lateinit var connectivityHelper: ConnectivityHelper

    /**
     * @brief Inicializa la activity de registro
     * @param savedInstanceState Estado guardado anterior
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        observeViewModel()
    }

    /**
     * @brief Configura los listeners de los botones
     * @details Establece las acciones para navegar al login y procesar el registro
     */
    private fun setupClickListeners() {
        binding.logBttn.setOnClickListener {
            navigateToLogin()
        }

        binding.regBttn.setOnClickListener {
            attemptRegister()
        }
    }

    /**
     * @brief Intenta registrar un nuevo usuario
     * @details Verifica conectividad, valida datos y procesa el registro
     */
    private fun attemptRegister() {
        if (!connectivityHelper.isNetworkAvailable()) {
            OfflineUtils.showNeedConnectionMessage(binding.root, "registrarse")
            return
        }

        val username = binding.registerName.text.toString()
        val email = binding.registerEmail.text.toString()
        val password = binding.registerPasword.text.toString()
        val rePassword = binding.registerRePasword.text.toString()

        if (validateInputs(username, email, password, rePassword)) {
            vm.register(username, email, password)
        }
    }

    /**
     * @brief Valida los datos de entrada del formulario
     * @details Verifica que todos los campos estén completos y sean válidos
     * @param username Nombre de usuario
     * @param email Dirección de correo electrónico
     * @param password Contraseña
     * @param rePassword Confirmación de contraseña
     * @return Boolean true si todos los datos son válidos, false en caso contrario
     */
    private fun validateInputs(username: String, email: String, password: String, rePassword: String): Boolean {
        var isValid = true

        if (username.isBlank()) {
            binding.registerName.error = "El nombre de usuario es requerido"
            isValid = false
        }

        if (email.isBlank()) {
            binding.emailTF.error = "El email es requerido"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailTF.error = "Email inválido"
            isValid = false
        }

        if (password.isBlank()) {
            binding.registerPasword.error = "La contraseña es requerida"
            isValid = false
        } else if (password.length < 6) {
            binding.registerPasword.error = "La contraseña debe tener al menos 6 caracteres"
            isValid = false
        }

        if (rePassword.isBlank()) {
            binding.registerRePasword.error = "Confirma la contraseña"
            isValid = false
        } else if (password != rePassword) {
            binding.registerRePasword.error = "Las contraseñas no coinciden"
            isValid = false
        }

        return isValid
    }

    /**
     * @brief Navega a la activity de login
     * @details Cierra la activity actual y abre LoginActivity
     */
    private fun navigateToLogin() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * @brief Observa los cambios en el ViewModel de registro
     * @details Reacciona a los diferentes estados del proceso de registro
     */
    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.user.collect { uiState ->
                    when (uiState) {
                        is UiState.Started -> {
                            // Estado inicial, no requiere acción
                        }
                        is UiState.Loading -> {
                            binding.regBttn.isEnabled = false
                        }
                        is UiState.Error -> {
                            binding.regBttn.isEnabled = true
                            Toast.makeText(this@RegisterActivity, uiState.message, Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Success -> {
                            binding.regBttn.isEnabled = true
                            Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                            navigateToLogin()
                        }
                    }
                }
            }
        }
    }
}