package com.example.kienldmbtvn.data.network.response

import com.google.gson.annotations.SerializedName

data class StyleResponse(
    val data: Data
)

data class Data(
    val items: List<CategoryItem> = emptyList(),
)

data class CategoryItem(
    @SerializedName("_id") val id: String,
    val name: String,
    val styles: List<StyleItem>
)

data class StyleItem(
    @SerializedName("_id") val id: String,
    val name: String,
    val key: String,
    val config: StyleConfig,
    val categories: List<String>,
    val segmentId: String,
    val styleType: String,
    val imageSize: String,
)

data class StyleConfig(
    val mode: Int,
    val imageSize: String,
    val baseModel: String,
    val style: String,
    val positivePrompt: String? = null,
    val negativePrompt: String? = null,
)