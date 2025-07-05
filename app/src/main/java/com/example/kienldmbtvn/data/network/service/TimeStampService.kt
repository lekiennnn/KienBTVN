package com.example.kienldmbtvn.data.network.service

import com.example.kienldmbtvn.data.network.response.TimeStampResponse
import retrofit2.Response
import retrofit2.http.GET

interface TimeStampService {
    @GET("/timestamp")
    suspend fun getTimestamp(): Response<TimeStampResponse>
}