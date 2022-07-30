package com.example.moviesearch.ui.log

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearch.databinding.ItemLogBinding

class LogAdapter(private val listener: (String) -> Unit) :
    ListAdapter<String, LogAdapter.LogViewHolder>(LogDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        return LogViewHolder(
            ItemLogBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LogViewHolder(private val binding: ItemLogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.log = item
            setClickLog(item)
        }

        // 검색 기록 선택시 검색 시작
        private fun setClickLog(item: String) {
            itemView.setOnClickListener {
                listener(item)
            }
        }

    }

    private object LogDiffUtil : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem.hashCode() == newItem.hashCode()

    }

}