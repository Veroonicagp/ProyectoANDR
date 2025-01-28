package com.example.readytoenjoy.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readytoenjoy.databinding.ActivityLoginBinding
import com.example.readytoenjoy.ui.MainActivity
import com.example.readytoenjoy.ui.adven.AdvenListAdapter
import com.example.readytoenjoy.ui.adven.AdvenListUiState
import com.example.readytoenjoy.ui.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logBttn.setOnClickListener() {
            val name = binding.logName.text.toString()
            val password = binding.logPssw.text.toString()
            viewModel.login(name, password)
            //val intent = Intent(this@LoginActivity, MainActivity::class.java)
            //startActivity(intent)
            //finish()
        }
        binding.regBttn.setOnClickListener() {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        //añadido nuevo
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    LoginUiState.Loading -> {}
                    is LoginUiState.Success -> {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    is LoginUiState.Error -> {

                    }
                }
            }

        }

    }
}