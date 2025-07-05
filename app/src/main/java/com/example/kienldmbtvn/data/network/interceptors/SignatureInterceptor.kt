package com.example.kienldmbtvn.data.network.interceptors

import com.apero.signature.SignatureParser
import com.example.kienldmbtvn.data.AiArtServiceEntry
import com.example.kienldmbtvn.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class SignatureInterceptor : Interceptor {
    companion object {
        private const val TAG = "SignatureInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val signatureData = SignatureParser.parseData(
            AiArtServiceEntry.API_KEY,
            BuildConfig.PUBLIC_KEY,
            AiArtServiceEntry.timeStamp
        )
        val tokenIntegrity =
            signatureData.tokenIntegrity.ifEmpty { com.example.kienldmbtvn.data.network.consts.ServiceConstants.NOT_GET_API_TOKEN }
        val headers = hashMapOf(
            "Accept" to "application/json",
            "Content-Type" to "application/json",
            "device" to "android",
//            "Authorization" to "Bearer ${BuildConfig.ART_BEAR_TOKEN}",
            "x-api-signature" to signatureData.signature,
            "x-api-timestamp" to signatureData.timeStamp.toString(),
            "x-api-keyid" to signatureData.keyId,
            "x-api-token" to tokenIntegrity,
            "x-api-bundleId" to AiArtServiceEntry.BUNDLE_ID,
            "App-name" to AiArtServiceEntry.APP_NAME,
        )
        val requestBuilder = chain.request().newBuilder()
        for ((key, value) in headers) {
            requestBuilder.addHeader(key, value)
        }
        return chain.proceed(requestBuilder.build())
    }
}