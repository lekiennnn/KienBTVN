package com.example.kienldmbtvn.data.exception

import androidx.annotation.StringRes
import com.example.kienldmbtvn.R

class AiArtException(
    val errorReason: ErrorReason
) : RuntimeException() {
    override fun toString(): String = errorReason.name
}

enum class ErrorReason(@StringRes val resMessage: Int) {
    NetworkError(R.string.network_error),
    InternalError(R.string.internal_error),
    UnknownError(R.string.unknown_error),
    ImageTypeInvalid(R.string.image_type_invalid),
    PresignedLinkError(R.string.unknown_error),
    GenerateImageError(R.string.generate_image_error)
}
