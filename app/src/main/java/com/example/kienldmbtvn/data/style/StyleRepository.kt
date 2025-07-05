package com.example.kienldmbtvn.data.style

import com.example.kienldmbtvn.data.network.response.CategoryItem
import com.example.kienldmbtvn.data.network.response.StyleItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface StyleRepository {
    suspend fun getItems(): Result<List<CategoryItem>>
    suspend fun getStyles(): Result<List<StyleItem>>
}

class StyleRepositoryImpl(private val styleApiService: StyleApiService) : StyleRepository {
    override suspend fun getItems(): Result<List<CategoryItem>> = withContext(Dispatchers.IO) {
        try {
            val response = styleApiService.getStyles()
            if (response.isSuccessful) {
                val items = response.body()?.data?.items ?: emptyList()
                Result.success(items)
            } else {
                Result.failure(Exception("API error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getStyles(): Result<List<StyleItem>> = withContext(Dispatchers.IO) {
        try {
            val response = styleApiService.getStyles()
            if (response.isSuccessful) {
                val styleItems = response.body()?.data?.items?.flatMap { it.styles } ?: emptyList()
                Result.success(styleItems)
            } else {
                Result.failure(Exception("API error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}