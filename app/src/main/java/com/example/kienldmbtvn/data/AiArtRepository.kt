package com.example.kienldmbtvn.data

import com.example.kienldmbtvn.data.network.response.Data
import com.example.kienldmbtvn.data.params.AiArtParams

interface AiArtRepository {
    suspend fun genArtAi(params: AiArtParams): Result<String>

    suspend fun getAllStyles(): Result<Data>
}