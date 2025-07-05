package com.example.kienldmbtvn.data.network.consts

object ServiceConstants {
    const val NOT_GET_API_TOKEN = "not_get_api_token"
    const val TIME_STAMP_NULL = "time_stamp_null"
    const val UNKNOWN_ERROR_MESSAGE = "unknown_error_message"
    const val ERROR_TIME_OUT_MESSAGE = "error_time_out_message"
    const val SAVE_FILE_ERROR = "save_file_failed"

    object RequestConstants {
        const val MAX_IMAGE_PIXEL = 1024
        const val MIN_IMAGE_PIXEL = 128
    }

    const val TIME_OUT_SECONDS = 30000L
    const val CODE_SUCCESS = 200
    const val CODE_UNKNOWN_ERROR = 9999
    const val CODE_FILE_NULL = 1000
    const val CODE_PARSING_ERROR = 1001
    const val CODE_TIMEOUT_ERROR = 1002
    const val CODE_PUSH_IMAGE_ERROR = 1003
    const val CODE_GET_LINK_ERROR = 1004
}