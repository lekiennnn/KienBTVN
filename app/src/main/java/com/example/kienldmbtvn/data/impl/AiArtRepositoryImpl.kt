package com.example.kienldmbtvn.data.impl

import android.content.Context
import android.util.Log
import com.example.kienldmbtvn.data.AiArtRepository
import com.example.kienldmbtvn.data.AiArtServiceEntry
import com.example.kienldmbtvn.data.exception.AiArtException
import com.example.kienldmbtvn.data.exception.ErrorReason
import com.example.kienldmbtvn.data.network.request.AiArtRequest
import com.example.kienldmbtvn.data.params.AiArtParams
import com.example.kienldmbtvn.data.network.response.Data
import com.example.kienldmbtvn.data.network.service.AiArtService
import com.example.kienldmbtvn.data.style.StyleApiService
import com.example.kienldmbtvn.data.utils.FileUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody

class AiArtRepositoryImpl(
    private val context: Context,
    private val aiArtService: AiArtService,
    private val styleService: StyleApiService
) : AiArtRepository {
    companion object {
        private const val TAG = "AiArtRepositoryImpl"
    }

    override suspend fun genArtAi(params: AiArtParams): Result<String> {
        try {
            Log.d(TAG, "genArtAi: start gen ${params.imageUri.path}")
            if (!FileUtils.checkImageExtension(context, params.imageUri)) {
                return Result.failure(AiArtException(ErrorReason.ImageTypeInvalid))
            }
            val imageBitmapResized = FileUtils.uriToResizedBitmap(
                context,
                params.imageUri,
                com.example.kienldmbtvn.data.network.consts.ServiceConstants.RequestConstants.MAX_IMAGE_PIXEL,
                com.example.kienldmbtvn.data.network.consts.ServiceConstants.RequestConstants.MIN_IMAGE_PIXEL
            )
            Log.d(TAG, "genArtAi: imageResized ${imageBitmapResized.config?.name}")
            val imageFile = FileUtils.saveBitmapToCache(
                context,
                imageBitmapResized,
                "image_${System.currentTimeMillis()}.jpg"
            )
            Log.d(TAG, "genArtAi: imageFile ${imageFile.absolutePath}")
            
            val presignedLinkResponse = aiArtService.getLink()
            Log.d(TAG, "genArtAi: presignedLink response code: ${presignedLinkResponse.code()}")
            
            if (!presignedLinkResponse.isSuccessful) {
                Log.e(TAG, "genArtAi: Failed to get presigned link - HTTP ${presignedLinkResponse.code()}: ${presignedLinkResponse.message()}")
                Log.e(TAG, "genArtAi: Error body: ${presignedLinkResponse.errorBody()?.string()}")
                return Result.failure(AiArtException(ErrorReason.PresignedLinkError))
            }
            
            val presignedLinkResponseBody = presignedLinkResponse.body()
            if (presignedLinkResponseBody == null) {
                Log.e(TAG, "genArtAi: Presigned link response body is null")
                return Result.failure(AiArtException(ErrorReason.PresignedLinkError))
            }
            
            val presignedLink = presignedLinkResponseBody.data
            if (presignedLink == null) {
                Log.e(TAG, "genArtAi: Presigned link data is null")
                return Result.failure(AiArtException(ErrorReason.PresignedLinkError))
            }
            
            Log.d(TAG, "genArtAi: presignedLink ${presignedLink.path}")
            AiArtServiceEntry.setTimeStamp(presignedLinkResponseBody.timestamp)
            val pushToServer = aiArtService.pushImageToServer(
                url = presignedLink.url,
                file = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull()),
            )
            Log.d(
                TAG,
                "genArtAi: pushToServer ${pushToServer.code()} ${pushToServer.message()} ${pushToServer.errorBody()}"
            )
            if (pushToServer.isSuccessful) {
                val request = createMultipartBodyAiArt(params, presignedLink.path)
                Log.d(TAG, "genArtAi: start calling genAI $request")
                val response = aiArtService.genArtAi(request)
                Log.d(
                    TAG,
                    "genArtAi: response ${response.raw()} error ${response.errorBody()?.string()}"
                )
                val urlResult = response.body()?.data?.url ?: ""
                return if (response.isSuccessful && urlResult.isNotEmpty())
                    Result.success(urlResult)
                else {
                    Log.d(TAG, "genArtAi: error when genAI ${response.message()}")
                    Result.failure(AiArtException(ErrorReason.GenerateImageError))
                }
            } else {
                Log.d(TAG, "genArtAi: error when pushToServer ${pushToServer.message()}")
                return Result.failure(AiArtException(ErrorReason.GenerateImageError))
            }
        } catch (e: Exception) {
            Log.e(TAG, "genArtAi error", e)
            return Result.failure(e)
        }
    }

    override suspend fun getAllStyles(): Result<Data> {
        return try {
            val response = styleService.getStyles()

            if (response.isSuccessful) {
                val data = response.body()?.data
                if (data != null) {
                    Result.success(data)
                } else {
                    Log.d(TAG, "getAllStyles: response body null ${response.raw()}")
                    Result.failure(AiArtException(ErrorReason.UnknownError))
                }
            } else {
                Log.d(TAG, "getAllStyles: error when getStyles ${response.message()}")
                Result.failure(AiArtException(ErrorReason.UnknownError))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getAllStyles error", e)
            Result.failure(AiArtException(ErrorReason.UnknownError))
        }
    }

    private fun createMultipartBodyAiArt(
        params: AiArtParams,
        image: String
    ): AiArtRequest {
        return AiArtRequest(
            file = image,
            styleId = params.styleId,
            positivePrompt = params.positivePrompt,
            negativePrompt = params.negativePrompt,
        )
    }
}