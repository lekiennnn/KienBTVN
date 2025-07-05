package com.example.kienldmbtvn.data.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.graphics.scale
import com.example.kienldmbtvn.data.exception.AiArtException
import com.example.kienldmbtvn.data.exception.ErrorReason
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object FileUtils {
    private const val TAG = "FileUtils"

    fun checkImageExtension(context: Context, uri: Uri): Boolean {
        val mimeType = context.contentResolver.getType(uri)
        return mimeType in listOf("image/jpeg", "image/jpg")
    }

    fun uriToResizedBitmap(
        context: Context,
        uri: Uri,
        maxDimension: Int,
        minDimension: Int
    ): Bitmap {
        val inputStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val width = originalBitmap.width
        val height = originalBitmap.height

        // Calculate scaling factor to fit within minDimension and maxDimension
        val scale = when {
            // Case 1: Both dimensions are within bounds, no scaling needed
            width in minDimension..maxDimension && height in minDimension..maxDimension -> 1.0f
            // Case 2: Scale up if both dimensions are below minDimension
            width < minDimension || height < minDimension -> {
                val scaleWidth = minDimension.toFloat() / width
                val scaleHeight = minDimension.toFloat() / height
                maxOf(
                    scaleWidth,
                    scaleHeight
                ) // Use the larger scale to ensure both dimensions are at least minDimension
            }
            // Case 3: Scale down if any dimension exceeds maxDimension
            else -> {
                val scaleWidth = maxDimension.toFloat() / width
                val scaleHeight = maxDimension.toFloat() / height
                minOf(
                    scaleWidth,
                    scaleHeight
                ) // Use the smaller scale to ensure both dimensions are at most maxDimension
            }
        }

        // Calculate new dimensions
        val newWidth = (width * scale).toInt().coerceIn(minDimension, maxDimension)
        val newHeight = (height * scale).toInt().coerceIn(minDimension, maxDimension)

        return originalBitmap.scale(newWidth, newHeight)
    }

    fun saveBitmapToCache(context: Context, bitmap: Bitmap, fileName: String): File {
        val cacheDir = context.cacheDir
        val file = File(cacheDir, fileName)

        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.flush()
        }

        return file
    }

    /**
     * Modern implementation for downloading files that works with scoped storage on Android 10+
     */
    suspend fun downloadImageToGallery(context: Context, fileUrl: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Starting download from URL: $fileUrl")
                // Download the file first
                val url = URL(fileUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                Log.d(TAG, "Connection response code: ${connection.responseCode}")
                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "HTTP request failed with code: ${connection.responseCode}")
                    return@withContext Result.failure(AiArtException(ErrorReason.UnknownError))
                }

                val inputStream: InputStream = connection.inputStream
                
                // Use MediaStore for modern Android versions (10+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Log.d(TAG, "Using MediaStore API for Android 10+")
                    val fileName = "AI_Art_${System.currentTimeMillis()}.jpg"
                    
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/AI Art")
                    }
                    
                    val contentResolver = context.contentResolver
                    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    
                    if (uri != null) {
                        Log.d(TAG, "MediaStore URI created: $uri")
                        contentResolver.openOutputStream(uri)?.use { outputStream ->
                            val buffer = ByteArray(4096)
                            var bytesRead: Int
                            var totalBytes = 0
                            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                                outputStream.write(buffer, 0, bytesRead)
                                totalBytes += bytesRead
                            }
                            outputStream.flush()
                            Log.d(TAG, "File saved successfully. Total bytes: $totalBytes")
                        }
                        
                        inputStream.close()
                        return@withContext Result.success(Unit)
                    } else {
                        Log.e(TAG, "Failed to create MediaStore URI")
                        inputStream.close()
                        return@withContext Result.failure(AiArtException(ErrorReason.UnknownError))
                    }
                } else {
                    Log.d(TAG, "Using legacy storage for Android 9 and below")
                    // Fallback for older Android versions
                    return@withContext saveFileToStorageLegacy(fileUrl)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Download failed with exception", e)
                e.printStackTrace()
                return@withContext Result.failure(e)
            }
        }
    }

    /**
     * Legacy implementation - kept for backward compatibility
     */
    private suspend fun saveFileToStorageLegacy(fileUrl: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(fileUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    return@withContext Result.failure(AiArtException(ErrorReason.UnknownError))
                }

                val inputStream: InputStream = connection.inputStream
                val downloadsDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) downloadsDir.mkdirs()

                val file =
                    File(downloadsDir, "image_apero_ai_art_${System.currentTimeMillis()}.jpg")

                val outputStream = FileOutputStream(file)
                val buffer = ByteArray(4096)
                var bytesRead: Int

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()
                return@withContext Result.success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext Result.failure(e)
            }
        }
    }

    @Deprecated("Use downloadImageToGallery instead for modern Android compatibility")
    suspend fun saveFileToStorage(fileUrl: String): Result<Unit> {
        return saveFileToStorageLegacy(fileUrl)
    }
}