package com.example.kienldmbtvn.data.params

import android.net.Uri

data class AiArtParams(
    val imageUri: Uri,
    val styleId: String? = null,
    val positivePrompt: String? = null,
    val negativePrompt: String? = null,
    val prompt: String? = null,
)