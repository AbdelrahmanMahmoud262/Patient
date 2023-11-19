package com.androdevelopment.patient.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androdevelopment.patient.util.PDFRenderService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

sealed class PDFReaderViewModelState{
    data class OnPDFFile(val file:File): PDFReaderViewModelState()
    data class Error(val message:String?): PDFReaderViewModelState()
    data class Progress(val progress:Int): PDFReaderViewModelState()

    data object Empty: PDFReaderViewModelState()
    data object Loading: PDFReaderViewModelState()
    data object None: PDFReaderViewModelState()


}

class PDFReaderViewModel:ViewModel() {

    private val _pdfReaderViewModelState = MutableStateFlow<PDFReaderViewModelState>(
        PDFReaderViewModelState.None
    )
    val pdfReaderViewModelState:StateFlow<PDFReaderViewModelState> = _pdfReaderViewModelState

    fun pdf(context: Context,url:String) = viewModelScope.launch {
        _pdfReaderViewModelState.value = PDFReaderViewModelState.Loading

        try{

            coroutineScope {
                val pdfService = async {
                    PDFRenderService.pdf(context,url){
                        Log.d("???","progress $it%")

                        _pdfReaderViewModelState.value = PDFReaderViewModelState.Progress(it)

                    }
                }

                val pdfFile = pdfService.await()

                if (pdfFile == null){
                    _pdfReaderViewModelState.value = PDFReaderViewModelState.Empty
                    return@coroutineScope
                }

                _pdfReaderViewModelState.value = PDFReaderViewModelState.OnPDFFile(pdfFile)
            }
        }
        catch (e:Exception){
            _pdfReaderViewModelState.value = PDFReaderViewModelState.Error(e.message)
        }

    }

}