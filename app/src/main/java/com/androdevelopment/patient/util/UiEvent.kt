package com.androdevelopment.patient.util

import com.androdevelopment.patient.data.models.Result

sealed class UiEvent {
    data object PopBackStack: UiEvent()
    data class Navigate(val route: String,val extras: Result? = null): UiEvent()
    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ): UiEvent()
}