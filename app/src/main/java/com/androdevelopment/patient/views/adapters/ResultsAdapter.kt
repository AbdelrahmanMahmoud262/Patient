package com.androdevelopment.patient.views.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.view.setMargins
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.androdevelopment.patient.databinding.ItemLatestResultsBinding
import com.androdevelopment.patient.data.models.Result



class ResultsAdapter(private val onResultsClick: OnResultsClick, private val isAll:Boolean) :
    ListAdapter<Result, ResultsAdapter.ResultsViewHolder>(
        ItemCallback
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        val binding =
            ItemLatestResultsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ResultsViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ResultsViewHolder(private val binding: ItemLatestResultsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.P)
        fun bind(item: Result) {
            binding.textViewTitle.text = item.title
            binding.textViewID.text = item.id.toString()
            binding.textViewDate.text = item.date.toString()
            binding.textViewLabName.text = item.labName
            binding.imageViewNew.visibility = if (item.isNew) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }

            if (item.id == currentList.last().id&& !isAll && itemCount != 1) {
                binding.viewAllContainer.visibility = View.VISIBLE
            }else{
                binding.viewAllContainer.visibility = View.INVISIBLE
            }

            binding.root.setOnClickListener {
                if (item.id == currentList.last().id && !isAll && itemCount != 1){
                    onResultsClick.onLastResultClick(item)
                }else {
                    onResultsClick.onResultClick(item)
                }
            }

            if (isAll){
                val layoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT).apply {
                    setMargins(20)
                }
                binding.root.layoutParams = layoutParams
            }
        }
    }

    private object ItemCallback : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(
            oldItem: Result,
            newItem: Result
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Result,
            newItem: Result
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface OnResultsClick {
        fun onResultClick(item: Result)
        fun onLastResultClick(item: Result)
    }
}