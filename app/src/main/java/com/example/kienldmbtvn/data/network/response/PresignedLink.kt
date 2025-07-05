package com.example.kienldmbtvn.data.network.response

import com.google.gson.annotations.SerializedName

data class PresignedLink(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Link?,
    @SerializedName("timestamp") val timestamp: Long
)

data class Link(
    @SerializedName("url") val url: String,
    @SerializedName("path") val path: String
)