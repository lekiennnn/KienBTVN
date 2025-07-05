package com.example.kienldmbtvn.ui.style

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.kienldmbtvn.base.BaseViewModel
import com.example.kienldmbtvn.base.BaseUIState
import com.example.kienldmbtvn.data.AiArtRepository
import com.example.kienldmbtvn.data.exception.AiArtException
import com.example.kienldmbtvn.data.exception.ErrorReason
import com.example.kienldmbtvn.data.network.consts.ServiceConstants
import com.example.kienldmbtvn.data.network.response.CategoryItem
import com.example.kienldmbtvn.data.network.response.StyleItem
import com.example.kienldmbtvn.data.params.AiArtParams
import com.example.kienldmbtvn.data.style.StyleRepository
import kotlinx.coroutines.launch

data class StyleData(
    val styles: List<StyleItem> = emptyList(),
    val selectedStyle: StyleItem? = null,
    val categories: List<CategoryItem> = emptyList(),
    val selectedCategory: CategoryItem? = null,
    val prompt: String = "",
    val imageUrl: Uri? = null,
    val isStyleLoading: Boolean = false,
    val styleError: String? = null,
    val isCategoryLoading: Boolean = false,
    val categoryError: String? = null,
    val generatingState: BaseUIState<String> = BaseUIState.Idle
)

class StyleViewModel(
    private val styleRepository: StyleRepository,
    private val aiArtRepository: AiArtRepository
) : BaseViewModel<BaseUIState<StyleData>>(BaseUIState.Success(StyleData())) {

    private var allStyles: List<StyleItem> = emptyList()

    init {
        fetchStyles()
        fetchCategories()
    }

    private fun updateStyleData(update: (StyleData) -> StyleData) {
        val currentState = uiState.value
        if (currentState is BaseUIState.Success) {
            updateState { BaseUIState.Success(update(currentState.data)) }
        }
    }

    private fun getCurrentData(): StyleData? {
        return when (val state = uiState.value) {
            is BaseUIState.Success -> state.data
            else -> null
        }
    }

    fun updatePrompt(newPrompt: String) {
        updateStyleData { it.copy(prompt = newPrompt) }
    }

    fun updateImageUrl(imageUri: Uri?) {
        updateStyleData { it.copy(imageUrl = imageUri) }
    }

    fun fetchStyles() {
        viewModelScope.launch {
            updateStyleData { it.copy(isStyleLoading = true) }
            styleRepository.getStyles()
                .onSuccess { styles ->
                    allStyles = styles
                    updateStyleData { currentData ->
                        currentData.copy(
                            styles = filterStylesByCategory(currentData.selectedCategory, styles),
                            isStyleLoading = false,
                            styleError = null
                        )
                    }
                }
                .onFailure { error ->
                    updateStyleData {
                        it.copy(
                            isStyleLoading = false,
                            styleError = "Network error: ${error.message ?: "Unknown error"}"
                        )
                    }
                }
        }
    }

    fun fetchCategories() {
        viewModelScope.launch {
            updateStyleData { it.copy(isCategoryLoading = true) }
            styleRepository.getItems()
                .onSuccess { categories ->
                    updateStyleData {
                        it.copy(
                            categories = categories,
                            isCategoryLoading = false,
                            categoryError = null
                        )
                    }
                }
                .onFailure { error ->
                    updateStyleData {
                        it.copy(
                            isCategoryLoading = false,
                            categoryError = "Network error: ${error.message ?: "Unknown error"}"
                        )
                    }
                    Log.d("StyleViewModel", "Network error: ${error.message}")
                }
        }
    }

    fun filterStylesByCategory(category: CategoryItem?, styles: List<StyleItem>): List<StyleItem> {
        return if (category == null) {
            styles
        } else {
            styles.filter { style ->
                style.categories.contains(category.id)
            }
        }
    }

    fun selectStyle(style: StyleItem) {
        updateStyleData { it.copy(selectedStyle = style) }
    }

    fun selectCategory(category: CategoryItem) {
        updateStyleData { currentData ->
            val filteredStyles = filterStylesByCategory(category, allStyles)
            currentData.copy(
                selectedCategory = category,
                styles = filteredStyles,
                selectedStyle = if (filteredStyles.contains(currentData.selectedStyle)) {
                    currentData.selectedStyle
                } else {
                    null
                }
            )
        }
    }

    fun generateImage(context: Context, onSuccess: (resultUrl: String) -> Unit) {
        updateStyleData {
            it.copy(generatingState = BaseUIState.Loading)
        }
        viewModelScope.launch {
            val currentData = getCurrentData()
            if (currentData?.imageUrl == null) {
                updateStyleData {
                    it.copy(generatingState = BaseUIState.Error("Image is required"))
                }
                return@launch
            }
            if (currentData.selectedStyle == null) {
                updateStyleData {
                    it.copy(generatingState = BaseUIState.Error("Style is required"))
                }
                return@launch
            }
            val genResult = aiArtRepository.genArtAi(
                params = AiArtParams(
                    prompt = currentData.prompt,
                    styleId = currentData.selectedStyle.id,
                    positivePrompt = currentData.prompt,
                    negativePrompt = currentData.prompt,
                    imageUri = currentData.imageUrl
                )
            )
            genResult.fold(
                onSuccess = { fileUrl ->
                    updateStyleData {
                        it.copy(generatingState = BaseUIState.Success(fileUrl))
                    }
                    onSuccess(fileUrl)
                    updateStyleData {
                        it.copy(generatingState = BaseUIState.Idle)
                    }
                },
                onFailure = { error ->
                    val message = when (error) {
                        is AiArtException -> {
                            when (error.errorReason) {
                                ErrorReason.PresignedLinkError -> {
                                    "Network error: Failed to get upload link. Please check your internet connection and try again."
                                }
                                ErrorReason.NetworkError -> {
                                    "Network error: Please check your internet connection and try again."
                                }
                                ErrorReason.ImageTypeInvalid -> {
                                    "Invalid image format. Please select a valid image file."
                                }
                                ErrorReason.GenerateImageError -> {
                                    "Image generation failed. Please try again."
                                }
                                else -> context.getString(error.errorReason.resMessage)
                            }
                        }
                        else -> {
                            error.message ?: ServiceConstants.UNKNOWN_ERROR_MESSAGE
                        }
                    }
                    updateStyleData {
                        it.copy(generatingState = BaseUIState.Error(message))
                    }
                }
            )
        }
    }
}