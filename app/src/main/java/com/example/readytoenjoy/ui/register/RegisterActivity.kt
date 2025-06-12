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

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private val vm: RegisterViewModel by viewModels()
    private lateinit var binding: ActivityRegisterBinding

    @Inject
    lateinit var connectivityHelper: ConnectivityHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.logBttn.setOnClickListener {
            navigateToLogin()
        }

        binding.regBttn.setOnClickListener {
            attemptRegister()
        }
    }

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

    private fun navigateToLogin() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.user.collect { uiState ->
                    when (uiState) {
                        is UiState.Started -> {}
                        is UiState.Loading -> {
                            binding.regBttn.isEnabled = true
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