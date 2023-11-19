package com.androdevelopment.patient.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androdevelopment.patient.data.database.Database
import com.androdevelopment.patient.data.models.Result
import com.androdevelopment.patient.data.repositories.home.MainRepository
import com.androdevelopment.patient.data.repositories.home.MainRepositoryImpl
import com.androdevelopment.patient.util.UiEvent
import com.androdevelopment.patient.views.events.HomeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {



    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    private val _items=  repository.getFirstFourOrderedByDate()
    val items = _items

    private val _state = MutableLiveData<Boolean>(true)
    val state = _state

    init {
        viewModelScope.launch {
            isLoading()
//            val dao = ResultsDatabase.getDatabase(context).getDao()
//            repository = MainRepositoryImpl(dao)
            repository.getFirstFourOrderedByDate().collect{
                Log.e("data",it.toString())
            }
        }
    }

    fun isLoading() {
        viewModelScope.launch {
            state.postValue(true)
            delay(2000)
            state.postValue(false)
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnResultsClick -> {
//                sendUiEvents(UiEvent.Navigate(Constants.RESULT_FRAGMENT))
                removeItem(event.result)
            }

            is HomeEvent.OnQrcodeClick -> {
                sendUiEvents(UiEvent.ShowSnackbar("qrcode clicked"))
                addItem(
                    Result(
                        0,"qrcode",
                        293814,
                        "News Lab",
                        "15-5-1005",
                        false,
                        false,
                        "Description",
                        "Technican comment",
                        ""
                    )
                )
            }

            is HomeEvent.OnNotificationClick -> {
                sendUiEvents(UiEvent.ShowSnackbar("notification clicked"))
                isLoading()
            }

            else -> {Unit}
        }
    }

    fun addItem(item: Result) {
        viewModelScope.launch {
            repository.insertResult(item)
        }
    }

    fun removeItem(item: Result) {
        viewModelScope.launch {
            repository.deleteResult(item)
        }
    }

    fun sendUiEvents(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}