package com.example.readytoenjoy.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.readytoenjoy.databinding.ActivityRegisterBinding
import com.example.readytoenjoy.ui.MainActivity
import com.example.readytoenjoy.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private val vm: RegisterViewModel by viewModels()
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.user.collect{uiState ->
                    when(uiState){
                        is UiState.Started -> {}
                        is UiState.Error ->{

                        }
                        is UiState.Success->{
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        is UiState.Loading -> {}
                    }
                }
            }
        }

        binding.logBttn.setOnClickListener(){
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.regBttn.setOnClickListener(){
            val username = binding.registerName.text.toString()
            val email = binding.registerEmail.text.toString()
            val password = binding.registerPasword.text.toString()
            val rePasword = binding.registerRePasword.text.toString()
            //val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            //startActivity(intent)
            //finish()
            if (email.isNullOrBlank()){
                binding.emailTF.error = "Introduzca email"
            }else {
                Log.d("arranca",username+email+password)
                vm.register(username,email,password)
            }
        }

    }
}