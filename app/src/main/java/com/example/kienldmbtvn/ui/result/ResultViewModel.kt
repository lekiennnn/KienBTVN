package com.example.kienldmbtvn.ui.result

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.kienldmbtvn.base.BaseViewModel
import com.example.kienldmbtvn.base.BaseUIState
import com.example.kienldmbtvn.data.utils.FileUtils
import kotlinx.coroutines.launch

data class ResultData(
    val downloadState: BaseUIState<Unit> = BaseUIState.Idle
)

class ResultViewModel(
    private val context: Context
) : BaseViewModel<BaseUIState<ResultData>>(BaseUIState.Success(ResultData())) {

    companion object {
        private const val TAG = "ResultViewModel"
    }

    private fun updateResultData(update: (ResultData) -> ResultData) {
        val currentState = uiState.value
        if (currentState is BaseUIState.Success) {
            updateState { BaseUIState.Success(update(currentState.data)) }
        }
    }

    private fun getCurrentData(): ResultData? {
        return when (val state = uiState.value) {
            is BaseUIState.Success -> state.data
            else -> null
        }
    }

    fun downloadPhoto(imageUrl: String) {
        viewModelScope.launch {
            Log.d(TAG, "Starting download for URL: $imageUrl")
            updateResultData { it.copy(downloadState = BaseUIState.Loading) }
            
            val result = FileUtils.downloadImageToGallery(context, imageUrl)
            
            result.fold(
                onSuccess = {
                    Log.d(TAG, "Download successful!")
                    updateResultData { it.copy(downloadState = BaseUIState.Success(Unit)) }
                    // Reset to idle after a short delay so UI can show success
                    updateResultData { it.copy(downloadState = BaseUIState.Idle) }
                },
                onFailure = { error ->
                    Log.e(TAG, "Download failed: ${error.message}", error)
                    val errorMessage = error.message ?: "Failed to download image"
                    updateResultData { it.copy(downloadState = BaseUIState.Error(errorMessage)) }
                }
            )
        }
    }

    fun clearDownloadState() {
        updateResultData { it.copy(downloadState = BaseUIState.Idle) }
    }
}