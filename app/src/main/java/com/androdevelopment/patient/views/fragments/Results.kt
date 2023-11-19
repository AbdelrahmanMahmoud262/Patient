package com.androdevelopment.patient.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView.GONE
import androidx.recyclerview.widget.RecyclerView.VISIBLE
import com.androdevelopment.patient.R
import com.androdevelopment.patient.data.models.Result
import com.androdevelopment.patient.databinding.FragmentResultsBinding
import com.androdevelopment.patient.util.Constants
import com.androdevelopment.patient.util.PreferenceManager
import com.androdevelopment.patient.util.UiEvent
import com.androdevelopment.patient.viewmodels.HomeViewModel
import com.androdevelopment.patient.viewmodels.ResultsViewModel
import com.androdevelopment.patient.views.adapters.ResultsAdapter
import com.androdevelopment.patient.views.events.ResultsEvent
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Results : Fragment() {


    private lateinit var binding: FragmentResultsBinding

    private var isFabOpened = false

    private val navController by lazy { findNavController() }

    private val viewModel by viewModels<ResultsViewModel>()

    private val preferenceManager by lazy { PreferenceManager(requireContext()) }

    private val resultsAdapter by lazy {
        ResultsAdapter(object : ResultsAdapter.OnResultsClick {
            override fun onResultClick(item: Result) {
                viewModel.onEvent(ResultsEvent.OnResultClick(item))
            }

            override fun onLastResultClick(item: Result) {
                viewModel.sendUiEvents(UiEvent.ShowSnackbar("Something went wrong", "Try Again"))
            }
        }, true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentResultsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        collectUiEvents()
        observeData()
        initFab()
        initRecyclerResults()
        initFilter()
        collectData()
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.selectedFilter.observe(viewLifecycleOwner) {
                setSelectedFilter(it)
                sortResults(it)
                if (binding.containerFilter.isVisible) binding.floatingActionButton.performClick()
            }
        }
    }

    private fun collectData() {
        viewModel.viewModelScope.launch {
            viewModel.resultsData.collect() {
                resultsAdapter.submitList(it)
                if (it.isEmpty()) binding.containerNoResults.visibility =
                    VISIBLE else binding.containerNoResults.visibility = View.INVISIBLE
            }
        }
    }

    private fun sortResults(sortby: Int?) {
        val list = ArrayList(resultsAdapter.currentList)
        list.sortBy {
            when (sortby) {
                Constants.STUDY_NAME_FILTER -> {
                    return@sortBy it.title
                }

                Constants.TESTING_CENTER_FILTER -> {
                    return@sortBy it.labName
                }

                Constants.CREATION_DATE_FILTER -> {
                    return@sortBy it.date.toString()
                }

                else -> {
                    return@sortBy it.title
                }
            }
        }
        resultsAdapter.submitList(list)
        binding.recyclerResults.smoothScrollToPosition(0)
    }

    private fun setSelectedFilter(filter: Int?) {
        when (filter) {
            Constants.STUDY_NAME_FILTER -> {
                binding.checkStudyName.visibility = VISIBLE
                binding.checkTestingCenter.visibility = View.INVISIBLE
                binding.checkCreationDate.visibility = View.INVISIBLE
            }

            Constants.TESTING_CENTER_FILTER -> {
                binding.checkTestingCenter.visibility = VISIBLE
                binding.checkCreationDate.visibility = View.INVISIBLE
                binding.checkStudyName.visibility = View.INVISIBLE
            }

            Constants.CREATION_DATE_FILTER -> {
                binding.checkCreationDate.visibility = VISIBLE
                binding.checkTestingCenter.visibility = View.INVISIBLE
                binding.checkStudyName.visibility = View.INVISIBLE
            }
        }
    }

    private fun initFilter() {
        binding.containerStudyName.setOnClickListener {
            viewModel.onEvent(ResultsEvent.OnFilterClick(Constants.STUDY_NAME_FILTER))
        }

        binding.containerCreationDate.setOnClickListener {
            viewModel.onEvent(ResultsEvent.OnFilterClick(Constants.CREATION_DATE_FILTER))
        }

        binding.containerTestingCenter.setOnClickListener {
            viewModel.onEvent(ResultsEvent.OnFilterClick(Constants.TESTING_CENTER_FILTER))
        }
    }

    private fun initRecyclerResults() {
        binding.recyclerResults.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = resultsAdapter
        }
    }

    private fun initFab() {
        binding.floatingActionButton.setOnClickListener {
            if (isFabOpened) {
                binding.containerFilter.visibility = GONE
                binding.containerFilter.startAnimation(
                    AnimationUtils.loadAnimation(
                        context, android.R.anim.fade_out
                    )
                )
                binding.floatingActionButton.setImageResource(R.drawable.sorting)
                isFabOpened = false
            } else {
                binding.containerFilter.visibility = VISIBLE
                binding.containerFilter.startAnimation(
                    AnimationUtils.loadAnimation(
                        context, android.R.anim.fade_in
                    )
                )
                binding.floatingActionButton.setImageResource(R.drawable.x)
                isFabOpened = true
            }
        }
    }

    private fun collectUiEvents() = viewModel.viewModelScope.launch {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT)
                        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setAction(event.action) {

                        }.show()
                }

                is UiEvent.Navigate -> {
                    when (event.route) {
                        Constants.RESULT_FRAGMENT -> {
//                            navController.navigate(R.id.action_results_to_result)
                            val intent = Intent(context, com.androdevelopment.patient.views.activities.Result::class.java)
                            intent.putExtra(Constants.RESULT_ID, event.extras?.id ?: -1)
                            startActivity(intent)
                        }
                    }
                }

                else -> Unit
            }
        }
    }

}