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
        return try {
            val jwt = loginRepository.getToken()
            val advenId = loginRepository.getAdvenId()
            !jwt.isNullOrEmpty() && !advenId.isNullOrEmpty()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun hasLocalUserData(): Boolean {
        return try {
            val advenId = loginRepository.getAdvenId()
            !advenId.isNullOrEmpty()
        } catch (e: Exception) {
            false
        }
    }
}