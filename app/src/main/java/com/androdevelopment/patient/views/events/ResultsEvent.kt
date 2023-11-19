package com.androdevelopment.patient.views.events

import com.androdevelopment.patient.data.models.Result

sealed class ResultsEvent {

    data class OnFilterClick(val filter:Int):ResultsEvent()
    data class OnResultClick(val item: Result):ResultsEvent()
    data object OnNotificationClick:ResultsEvent()
}