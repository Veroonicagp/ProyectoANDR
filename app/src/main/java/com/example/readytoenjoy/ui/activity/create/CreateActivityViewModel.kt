package com.example.readytoenjoy.ui.activity.create

import android.annotation.SuppressLint
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readytoenjoy.core.data.activity.ActivityRepositoryInterface
import com.example.readytoenjoy.core.data.activity.DefaultActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiState {

    object Started: UiState()
    object Loading: UiState()
    class Created(val id:String): UiState()
    class Error(val message: String): UiState()
}

@HiltViewModel
class CreateActivityViewModel @Inject constructor(private val repository: ActivityRepositoryInterface):ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState>
        get()= _uiState.asStateFlow()

    private val _photo = MutableStateFlow<Uri>(Uri.EMPTY)
    val photo: StateFlow<Uri>
        get() = _photo.asStateFlow()


    fun onImageCaptured(uri: Uri?) {
        viewModelScope.launch {
            uri?.let {
                _photo.value = uri
            }
        }

    }

    @SuppressLint("MissingPermission")
    fun create( title: String, location:String, price:String, description: String){
        viewModelScope.launch {
            val result = repository.createActivity(
                title,
                //img,
                location,
                price,
                description,
                //advenId
            )
            if(result.isSuccess){
                _uiState.value = UiState.Created(result.getOrNull()!!.id)
            }
        }
    }


}