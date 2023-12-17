package com.androdevelopment.patient.views.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.androdevelopment.patient.data.models.Result
import com.androdevelopment.patient.databinding.ActivityResultBinding
import com.androdevelopment.patient.util.Constants
import com.androdevelopment.patient.util.UiEvent
import com.androdevelopment.patient.viewmodels.ResultViewModel
import com.androdevelopment.patient.views.events.ResultEvent
import com.androdevelopment.patient.viewmodels.PDFReaderViewModel
import com.androdevelopment.patient.viewmodels.PDFReaderViewModelState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Result : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    private var resultId: Int = -1

    private val viewModel by viewModels<ResultViewModel>()

    private val pdfViewModel by viewModels<PDFReaderViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resultId = intent.getIntExtra(Constants.RESULT_ID, -1)

//        requestPermission()
//        setListeners()
//        collectUiEvents()
//        initResult()
//        loadPdf()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {

            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) -> {
                    requestPermissionLauncher.launch(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
            }

            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {

            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                requestPermissionLauncher.launch(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }

            else -> {
                requestPermissionLauncher.launch(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                requestPermissionLauncher.launch(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
        }
    }


    private fun initResult() = viewModel.viewModelScope.launch {
        if (resultId == -1) {
            viewModel.sendUiEvent(UiEvent.ShowSnackbar("Error \nPlease try again"))
            delay(1000)
            viewModel.onEvent(ResultEvent.OnBackStackClick)
            return@launch
        }
        val result = viewModel.getResultById(resultId)

        initData(result)
    }

    private fun initData(result: Result) {
        binding.resultId.text = result.reportId.toString()
        binding.labName.text = result.labName
        binding.resultDate.text = result.date
        binding.resultName.text = result.title
        binding.resultDescription.text = result.description
        binding.technicianComment.text = result.technicianComment
    }

    private fun loadPdf() {
        lifecycleScope.launch{
                pdfViewModel.pdfReaderViewModelState.collect{
                    when(it){
                        PDFReaderViewModelState.Empty -> {
                            viewModel.sendUiEvent(UiEvent.ShowSnackbar("PDF is empty"))
                        }
                        is PDFReaderViewModelState.Error -> {
                            viewModel.sendUiEvent(UiEvent.ShowSnackbar(it.message.toString()))
                        }
                        PDFReaderViewModelState.Loading -> TODO()
                        PDFReaderViewModelState.None -> Unit
                        is PDFReaderViewModelState.OnPDFFile -> TODO()
                        is PDFReaderViewModelState.Progress -> TODO()
                    }
                }
        }
        binding.webview.loadUrl("https://docs.google.com/gview?embedded=true&url=https://unec.edu.az/application/uploads/2014/12/pdf-sample.pdf")
    }

    private fun setListeners() {
        binding.buttonReadDocument.setOnClickListener { viewModel.onEvent(ResultEvent.OnReadDocumentClick) }
        binding.back.setOnClickListener { viewModel.onEvent(ResultEvent.OnBackStackClick) }
    }

    private fun collectUiEvents() {

        viewModel.viewModelScope.launch {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.Navigate -> {

                    }

                    UiEvent.PopBackStack -> {
                        onBackPressed()
                    }

                    is UiEvent.ShowSnackbar -> {
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT)
                            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
                    }
                }
            }
        }

    }


}

