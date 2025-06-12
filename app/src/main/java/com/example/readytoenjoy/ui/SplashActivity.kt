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

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    @Inject
    lateinit var connectivityHelper: ConnectivityHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startApp()
    }

    private fun startApp() {
        lifecycleScope.launch {
            delay(2500) // Tu tiempo original

            if (connectivityHelper.isNetworkAvailable()) {
                checkLoginOnline()
            } else {
                handleOfflineStart()
            }
        }
    }

    private suspend fun checkLoginOnline() {
        val isLoggedIn = viewModel.isUserLoggedIn()

        if (isLoggedIn) {
            navigateToMain()
        } else {
            navigateToLogin()
        }
    }

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

    private fun showOfflineToast() {
        Toast.makeText(
            this,
            "Sin conexión. Usando datos guardados.",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showNeedConnectionToast() {
        Toast.makeText(
            this,
            "Se requiere conexión para el primer inicio.",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun navigateToMain() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        finish()
    }
}