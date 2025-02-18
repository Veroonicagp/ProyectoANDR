package com.example.readytoenjoy.ui.activity.edit

import androidx.lifecycle.ViewModel
import com.example.readytoenjoy.core.data.repository.activity.ActivityRepositoryInterface
import com.example.readytoenjoy.core.data.repository.adven.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditActivityViewModel@Inject constructor(private val repository: ActivityRepositoryInterface, private val loginRepository: LoginRepository):ViewModel() {
}