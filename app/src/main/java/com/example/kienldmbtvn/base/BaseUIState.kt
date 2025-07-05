package com.example.kienldmbtvn.base

sealed class BaseUIState<out T> {
    object Loading : BaseUIState<Nothing>()
    data class Success<T>(val data: T) : BaseUIState<T>()
    data class Error(val message: String) : BaseUIState<Nothing>()
    data object Idle : BaseUIState<Nothing>()

    fun isLoading() = this is Loading
    fun isSuccess() = this is Success
    fun isError() = this is Error
    fun isIdle() = this is Idle
}