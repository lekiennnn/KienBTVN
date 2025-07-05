package com.example.kienldmbtvn.data.network.response

import com.google.gson.annotations.SerializedName

data class AiArtResponse(
    @SerializedName("data") val data: AiArtResponseData
)

data class AiArtResponseData(
    @SerializedName("path") val path: String? = "",
    @SerializedName("url") val url: String = ""
)