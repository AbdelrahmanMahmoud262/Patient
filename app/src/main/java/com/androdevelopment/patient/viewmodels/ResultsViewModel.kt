package com.androdevelopment.patient.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androdevelopment.patient.data.database.Database
import com.androdevelopment.patient.data.models.Result
import com.androdevelopment.patient.data.repositories.results.ResultsRepository
import com.androdevelopment.patient.data.repositories.results.ResultsRepositoryImpl
import com.androdevelopment.patient.util.Constants
import com.androdevelopment.patient.util.UiEvent
import com.androdevelopment.patient.views.events.ResultsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(private val repository: ResultsRepository): ViewModel() {


    private val _resultsData = repository.getResults()
    val resultsData = _resultsData

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableLiveData<Boolean>(true)
    val state = _state

    private val _selectedFilter = MutableLiveData(Constants.STUDY_NAME_FILTER)
    val selectedFilter = _selectedFilter

    fun addResult(item: Result) {
        viewModelScope.launch {
//            val currentList = _resultsData.value?.toMutableList() ?: mutableListOf()
//            currentList.add(item)
//            _resultsData.value = currentList as ArrayList<Result>
            repository.insertResult(item)
        }
    }

    fun removeResult(item: Result) {
        viewModelScope.launch {
//            val currentList = _resultsData.value?.toMutableList() ?: mutableListOf()
//            currentList.remove(item)
//            _resultsData.value = currentList as ArrayList<Result>
            repository.deleteResult(item)
        }
    }


    fun onEvent(event: ResultsEvent) {
        when (event) {
            is ResultsEvent.OnResultClick -> {
                sendUiEvents(UiEvent.Navigate(Constants.RESULT_FRAGMENT,event.item))
            }
            is ResultsEvent.OnFilterClick->{
                _selectedFilter.postValue(event.filter)
            }
            is ResultsEvent.OnNotificationClick -> {

            }
        }
    }

    fun sendUiEvents(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}