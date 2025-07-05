package com.example.kienldmbtvn.data.network.service

import com.example.kienldmbtvn.data.network.request.AiArtRequest
import com.example.kienldmbtvn.data.network.response.AiArtResponse
import com.example.kienldmbtvn.data.network.response.PresignedLink
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Url

interface AiArtService {

    @PUT
    suspend fun pushImageToServer(
        @Url url: String,
        @Body file: RequestBody
    ): Response<ResponseBody>

    @GET("/api/v5/image-ai/presigned-link")
    suspend fun getLink(): Response<PresignedLink>

    @POST("/api/v5/image-ai")
    suspend fun genArtAi(
        @Body requestBody: AiArtRequest,
    ): Response<AiArtResponse>
}