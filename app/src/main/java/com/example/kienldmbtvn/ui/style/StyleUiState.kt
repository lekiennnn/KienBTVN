package com.example.kienldmbtvn.ui.style

import android.net.Uri
import com.example.kienldmbtvn.base.BaseUIState
import com.example.kienldmbtvn.data.network.response.CategoryItem
import com.example.kienldmbtvn.data.network.response.StyleItem

data class StyleUiState(
    val styles: List<StyleItem> = emptyList(),
    val selectedStyle: StyleItem? = null,
    val isStyleLoading: Boolean = false,
    val styleError: String? = null,

    val categories: List<CategoryItem> = emptyList(),
    val selectedCategory: CategoryItem? = null,
    val isCategoryLoading: Boolean = false,
    val categoryError: String? = null,

    val prompt: String = "",
    val imageUrl: Uri? = null,
    val generatingState: BaseUIState<String> = BaseUIState.Idle
)