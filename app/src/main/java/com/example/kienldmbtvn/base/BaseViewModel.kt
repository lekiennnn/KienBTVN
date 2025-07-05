package com.example.kienldmbtvn.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<STATE>(
    initialState: STATE
) : ViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<STATE> = _uiState.asStateFlow()

    protected fun updateState(update: (STATE) -> STATE) {
        _uiState.update(update)
    }

    protected fun setState(newState: STATE) {
        _uiState.value = newState
    }

    protected val currentState: STATE
        get() = _uiState.value
}