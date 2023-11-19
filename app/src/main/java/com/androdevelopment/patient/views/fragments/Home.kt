package com.androdevelopment.patient.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.androdevelopment.patient.R
import com.androdevelopment.patient.data.models.Result
import com.androdevelopment.patient.databinding.FragmentHomeBinding
import com.androdevelopment.patient.util.Constants
import com.androdevelopment.patient.util.UiEvent
import com.androdevelopment.patient.viewmodels.HomeViewModel
import com.androdevelopment.patient.views.adapters.ResultsAdapter
import com.androdevelopment.patient.views.events.HomeEvent
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding


    private val homeViewModel by viewModels<HomeViewModel>()
    private val navController by lazy { findNavController() }
    private val resultsAdapter by lazy {
        ResultsAdapter(object : ResultsAdapter.OnResultsClick {
            override fun onResultClick(item: Result) {
                homeViewModel.onEvent(HomeEvent.OnResultsClick(item))
            }

            override fun onLastResultClick(item: Result) {
                homeViewModel.sendUiEvents(UiEvent.Navigate(Constants.RESULTS_FRAGMENT))
            }
        }, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        observeState()
        setUpResultsRecycler()
        collectUiEvents()
        setEvents()
        observeData()
    }

    private fun observeState() {
        homeViewModel.state.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.shimmerViewContainer.visibility = View.VISIBLE
                binding.recyclerMain.visibility = View.INVISIBLE
                binding.shimmerViewContainer.startShimmer()
            } else {
                binding.shimmerViewContainer.stopShimmer()
                binding.shimmerViewContainer.visibility = View.INVISIBLE
                binding.recyclerMain.visibility = View.VISIBLE
            }
        }
    }

    private fun observeData() {
//        homeViewModel.items.observe(viewLifecycleOwner) { models ->
//            resultsAdapter.submitList(models)
//            if (models.isEmpty()) binding.noResults.visibility = View.VISIBLE
//            else binding.noResults.visibility = View.INVISIBLE
//
//        }
        homeViewModel.viewModelScope.launch {
            homeViewModel.items.collect { results ->
                resultsAdapter.submitList(results)

                if (results.isEmpty()) binding.noResults.visibility = View.VISIBLE
                else binding.noResults.visibility = View.INVISIBLE
            }
        }
    }

    private fun setEvents() {
        binding.qrcode.setOnClickListener {
            homeViewModel.onEvent(HomeEvent.OnQrcodeClick)
        }
        binding.notification.setOnClickListener {
            homeViewModel.onEvent(HomeEvent.OnNotificationClick)
        }
    }

    private fun collectUiEvents() = homeViewModel.viewModelScope.launch {
        homeViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT)
                        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
                }

                is UiEvent.Navigate -> {
                    when (event.route) {
                        Constants.RESULTS_FRAGMENT -> {
                            navController.navigate(R.id.action_home_navigation_fragment_to_results_navigation_fragment)
                        }

                        Constants.RESULT_FRAGMENT -> {
//                            navController.navigate(R.id.action_home_to_result)
                            val intent = Intent(context,com.androdevelopment.patient.views.activities.Result::class.java)
                            startActivity(intent)
                        }
                    }
                }

                else -> Unit
            }
        }
    }

    private fun setUpResultsRecycler() {
        binding.recyclerMain.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = resultsAdapter
        }
    }
}