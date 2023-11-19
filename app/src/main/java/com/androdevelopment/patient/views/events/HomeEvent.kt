package com.androdevelopment.patient.views.events

import com.androdevelopment.patient.data.models.Result

sealed class HomeEvent {
    data class OnResultsClick(val result: Result):HomeEvent()
    data object OnQrcodeClick:HomeEvent()
    data object OnNotificationClick:HomeEvent()
}