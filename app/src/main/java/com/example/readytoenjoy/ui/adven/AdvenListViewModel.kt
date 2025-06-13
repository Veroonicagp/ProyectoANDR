package com.example.readytoenjoy.ui.adven

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.model.Adven
import com.example.readytoenjoy.core.data.repository.adven.AdvenRepositoryInterface
import com.example.readytoenjoy.core.data.repository.activity.ActivityRepositoryInterface
import com.example.readytoenjoy.core.data.repository.user.UserRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AdvenListViewModel @Inject constructor(
    private val defaultAdvenRepository: AdvenRepositoryInterface,
    private val activityRepository: ActivityRepositoryInterface,
    private val userRepository: UserRepositoryInterface
): ViewModel() {

    private val _uiState = MutableStateFlow<AdvenListUiState>(AdvenListUiState.Loading)
    val uiState: StateFlow<AdvenListUiState>
        get() = _uiState.asStateFlow()

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean>
        get() = _isAdmin.asStateFlow()

    private val _deleteResult = MutableStateFlow<DeleteResult?>(null)
    val deleteResult: StateFlow<DeleteResult?>
        get() = _deleteResult.asStateFlow()

    init {
        loadAdvens()
        observeAdvens()
        checkAdminStatus()
    }

    private fun checkAdminStatus() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val currentUser = userRepository.getCurrentUser()
                    withContext(Dispatchers.Main) {
                        _isAdmin.value = currentUser?.isAdmin == true
                    }
                }
            } catch (e: Exception) {
                _isAdmin.value = false
            }
        }
    }

    private fun observeAdvens() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.Main) {
                    defaultAdvenRepository.setStream.collect { advenList ->
                        if (advenList.isEmpty()) {
                            _uiState.value = AdvenListUiState.Loading
                        } else {
                            _uiState.value = AdvenListUiState.Success(advenList)
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = AdvenListUiState.Error("Error al observar aventureros: ${e.message}")
            }
        }
    }

    private fun loadAdvens() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val advens = defaultAdvenRepository.getAdvens()
                    withContext(Dispatchers.Main) {
                        if (advens.isNotEmpty()) {
                            _uiState.value = AdvenListUiState.Success(advens)
                        } else {
                            _uiState.value = AdvenListUiState.Loading
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = AdvenListUiState.Error("Error al cargar aventureros: ${e.message}")
            }
        }
    }

    fun deleteAdvenAndUser(advenId: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val activitiesResult = activityRepository.getActivitiesByAdvenId(advenId)
                    activitiesResult.getOrNull()?.forEach { activity ->
                        activityRepository.deleteActivity(activity.id)
                    }

                    val advenDeleteResult = defaultAdvenRepository.deleteAdven(advenId)

                    withContext(Dispatchers.Main) {
                        if (advenDeleteResult.isSuccess) {
                            _deleteResult.value = DeleteResult.Success("Aventurero y actividades eliminados correctamente")
                        } else {
                            _deleteResult.value = DeleteResult.Error("Error al eliminar aventurero")
                        }
                    }
                }
            } catch (e: Exception) {
                _deleteResult.value = DeleteResult.Error("Error inesperado al eliminar: ${e.message}")
            }
        }
    }

    fun clearDeleteResult() {
        _deleteResult.value = null
    }
}

sealed class AdvenListUiState {
    data object Loading: AdvenListUiState()
    class Success(val advenList: List<Adven>): AdvenListUiState()
    class Error(val message: String): AdvenListUiState()
}

sealed class DeleteResult {
    class Success(val message: String): DeleteResult()
    class Error(val message: String): DeleteResult()
}