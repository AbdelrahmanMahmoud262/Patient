package com.androdevelopment.patient.views.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.androdevelopment.patient.data.models.FilterModel
import com.androdevelopment.patient.databinding.ItemFilterBinding

class FilterAdapter(private val onFilterClick: OnFilterClick) :
    ListAdapter<FilterModel, FilterAdapter.FilterViewHolder>(ItemCallback) {

    private lateinit var binding: ItemFilterBinding

    inner class FilterViewHolder(binding: ItemFilterBinding) : ViewHolder(binding.root)

    private object ItemCallback : DiffUtil.ItemCallback<FilterModel>() {
        override fun areItemsTheSame(
            oldItem: FilterModel, newItem: FilterModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FilterModel, newItem: FilterModel
        ): Boolean {
            Log.e("item 1",oldItem.toString())
            Log.e("item 2",newItem.toString())
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {

        val item = getItem(position)

        binding.textView10.text = item.title
        binding.imageView4.visibility = if (item.isChecked) View.VISIBLE else View.INVISIBLE

        binding.root.setOnClickListener {
            onFilterClick.onFilterClick(item)
        }
    }

    interface OnFilterClick {
        fun onFilterClick(item: FilterModel)
    }
}

