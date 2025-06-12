package com.example.readytoenjoy.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.readytoenjoy.core.utils.ConnectivityHelper
import com.example.readytoenjoy.databinding.ActivityLoginBinding
import com.example.readytoenjoy.ui.MainActivity
import com.example.readytoenjoy.ui.register.RegisterActivity
import com.example.readytoenjoy.ui.utils.OfflineUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var connectivityHelper: ConnectivityHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.logBttn.setOnClickListener {
            attemptLogin()
        }

        binding.regBttn.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun attemptLogin() {
        if (!connectivityHelper.isNetworkAvailable()) {
            OfflineUtils.showNeedConnectionMessage(binding.root, "iniciar sesión")
            return
        }

        val name = binding.logName.text.toString()
        val password = binding.logPssw.text.toString()

        if (validateInputs(name, password)) {
            viewModel.login(name, password)
        }
    }

    private fun validateInputs(name: String, password: String): Boolean {
        var isValid = true

        if (name.isBlank()) {
            Toast.makeText(this, "El campo de usuario es obligatorio", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (password.isBlank()) {
            Toast.makeText(this, "El campo de password es obligatorio", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun navigateToRegister() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is LoginUiState.Success -> {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is LoginUiState.Error -> {
                        Toast.makeText(this@LoginActivity, uiState.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                    }
                }
            }
        }
    }
}