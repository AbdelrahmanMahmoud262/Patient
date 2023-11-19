package com.androdevelopment.patient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androdevelopment.patient.data.models.Result
import com.androdevelopment.patient.data.repositories.results.ResultsRepository
import com.androdevelopment.patient.util.UiEvent
import com.androdevelopment.patient.views.events.ResultEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(private val repository: ResultsRepository) : ViewModel() {


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(true)
    val state = _state

    suspend fun getResultById(resultId: Int): com.androdevelopment.patient.data.models.Result {
            return repository.getResult(resultId)
    }


    fun onEvent(event: ResultEvent) {
        when (event) {
            is ResultEvent.OnBackStackClick -> {
                sendUiEvent(UiEvent.PopBackStack)
            }

            is ResultEvent.OnReadDocumentClick -> {
                sendUiEvent(UiEvent.ShowSnackbar("Read document"))
            }
        }
    }

    fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}