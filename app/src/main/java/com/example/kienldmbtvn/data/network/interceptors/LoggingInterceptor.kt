package com.example.kienldmbtvn.data.network.interceptors

import okhttp3.logging.HttpLoggingInterceptor

fun createLoggingInterceptor(): HttpLoggingInterceptor {
    val logLevel = HttpLoggingInterceptor.Level.BODY
    return HttpLoggingInterceptor().apply { level = logLevel }
}