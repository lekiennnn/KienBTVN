package com.example.kienldmbtvn.data.style

import com.example.kienldmbtvn.data.network.response.StyleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StyleApiService {
    @GET("category")
    suspend fun getStyles(
        @Query("project") project: String = "techtrek",
        @Query("segmentValue") segmentValue: String = "IN",
        @Query("styleType") styleType: String = "imageToImage",
        @Query("isApp") isApp: Boolean = true
    ): Response<StyleResponse>
}