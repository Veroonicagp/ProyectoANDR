package com.example.readytoenjoy.ui.adven

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.model.Adven
import com.example.readytoenjoy.core.data.repository.adven.AdvenRepositoryInterface
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
    private val defaultAdvenRepository: AdvenRepositoryInterface
): ViewModel() {

    private val _uiState = MutableStateFlow<AdvenListUiState>(AdvenListUiState.Loading)
    val uiState: StateFlow<AdvenListUiState>
        get() = _uiState.asStateFlow()

    init {
        loadAdvens()
        observeAdvens()
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
                    // getAdvens() devuelve List<Adven> directamente, no Result<>
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
}

sealed class AdvenListUiState {
    data object Loading: AdvenListUiState()
    class Success(val advenList: List<Adven>): AdvenListUiState()
    class Error(val message: String): AdvenListUiState()
}