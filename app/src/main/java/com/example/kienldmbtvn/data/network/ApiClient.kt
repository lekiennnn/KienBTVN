package com.example.kienldmbtvn.data.network

import com.example.kienldmbtvn.data.AiArtServiceEntry
import com.example.kienldmbtvn.data.network.interceptors.SignatureInterceptor
import com.example.kienldmbtvn.data.network.service.AiArtService
import com.example.kienldmbtvn.data.network.service.TimeStampService
import com.example.kienldmbtvn.data.style.StyleApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val TIMEOUT_MILLIS = 60000L

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .readTimeout(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .writeTimeout(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(SignatureInterceptor())
            .addInterceptor(com.example.kienldmbtvn.data.network.interceptors.createLoggingInterceptor())
            .build()
    }

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private fun buildRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun getTimeStampService(): TimeStampService =
        buildRetrofit(AiArtServiceEntry.ART_SERVICE_URL).create()

    fun getStyleService(): StyleApiService = buildRetrofit(AiArtServiceEntry.ART_STYLE_URL).create()
    fun getAiArtService(): AiArtService {
        return buildRetrofit(AiArtServiceEntry.ART_SERVICE_URL).create(AiArtService::class.java)
    }
}