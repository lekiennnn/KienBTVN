package com.example.kienldmbtvn.ui.photopicker

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.kienldmbtvn.base.BaseViewModel
import com.example.kienldmbtvn.base.BaseUIState
import kotlinx.coroutines.launch

data class PhotoData(
    val photos: List<Photo> = emptyList(),
    val selectedPhoto: Photo? = null
)

class PhotoPickerViewModel(private val repository: PhotoRepository) : BaseViewModel<BaseUIState<PhotoData>>(BaseUIState.Loading) {

    fun loadPhotos() {
        viewModelScope.launch {
            updateState { BaseUIState.Loading }
            try {
                val photos = repository.getPhotos()
                updateState { BaseUIState.Success(PhotoData(photos = photos)) }
            } catch (e: Exception) {
                updateState { BaseUIState.Error("Failed to load photos: ${e.message}") }
            }
        }
    }

    fun togglePhotoSelection(photo: Photo) {
        val current = uiState.value
        if (current is BaseUIState.Success) {
            val newSelected = if (current.data.selectedPhoto == photo) {
                null
            } else {
                photo
            }
            updateState { BaseUIState.Success(current.data.copy(selectedPhoto = newSelected)) }
        }
    }

    fun getSelectedPhotoUri(): Uri? {
        return when (val state = uiState.value) {
            is BaseUIState.Success -> state.data.selectedPhoto?.uri
            else -> null
        }
    }
}