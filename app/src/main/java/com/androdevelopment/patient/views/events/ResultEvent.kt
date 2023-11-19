package com.androdevelopment.patient.views.events

sealed class ResultEvent{

    data object OnReadDocumentClick:ResultEvent()
    data object OnBackStackClick:ResultEvent()
}