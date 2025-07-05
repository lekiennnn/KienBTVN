package com.example.kienldmbtvn.data.style

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object StyleApiClient {
    private const val BASE_URL = "https://api-style-manager.apero.vn/"
    private const val TIMEOUT_MILLIS = 60000L

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .readTimeout(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .writeTimeout(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: StyleApiService by lazy {
        retrofit.create(StyleApiService::class.java)
    }
}
