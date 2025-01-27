package com.example.readytoenjoy.ui.adven

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.data.adven.Adven
import com.example.readytoenjoy.core.data.adven.AdvenRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import javax.inject.Inject

//@HiltViewModel// me da error si lo pongo
class AdvenListViewModel @Inject constructor(
    private val defaultAdvenRepository: AdvenRepositoryInterface
): ViewModel(){

    private val _uiState = MutableStateFlow<AdvenListUiState>(AdvenListUiState.Loading)
    val uiState: StateFlow<AdvenListUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Main){
                defaultAdvenRepository.setStream.collect{
                    advenList ->
                    if (advenList.isEmpty()) _uiState.value = AdvenListUiState.Loading
                    else _uiState.value = AdvenListUiState.Success(advenList)

                }
            }
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                defaultAdvenRepository.getAdvens()
            }
        }

    }

}

sealed class AdvenListUiState() {
    data object Loading: AdvenListUiState()
    class Success(val advenList: List<Adven>): AdvenListUiState()
    class Error(val message: String): AdvenListUiState()
}