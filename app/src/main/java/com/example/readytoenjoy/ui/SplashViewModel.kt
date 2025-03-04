package com.example.readytoenjoy.ui

import androidx.lifecycle.ViewModel
import com.example.readytoenjoy.core.data.repository.adven.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    suspend fun isUserLoggedIn(): Boolean {
        val jwt = loginRepository.getToken()
        val advenId = loginRepository.getAdvenId()

        return !jwt.isNullOrEmpty() && !advenId.isNullOrEmpty()
    }
}