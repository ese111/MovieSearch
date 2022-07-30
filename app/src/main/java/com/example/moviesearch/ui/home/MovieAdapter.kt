package com.example.moviesearch.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearch.data.model.MovieResult
import com.example.moviesearch.databinding.ItemMovieBinding
import com.example.moviesearch.databinding.ItemProgressbarBinding

private const val FOOTER = 0
private const val CONTENTS = 1

class MovieAdapter(private val listener: (String) -> Unit) :
    ListAdapter<MovieResult.ItemResult, RecyclerView.ViewHolder>(MovieDiffUtil) {

    var isLast = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CONTENTS -> {
                MovieViewHolder(
                    ItemMovieBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                ProgressViewHolder(
                    ItemProgressbarBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieViewHolder -> holder.bind(getItem(position))
            is ProgressViewHolder -> holder.bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            currentList.size -> FOOTER
            else -> CONTENTS
        }
    }

    // progressBar 추가를 위해서 +1
    override fun getItemCount(): Int {
        return  currentList.size + 1
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MovieResult.ItemResult) {
            binding.item = item
            setItemClick(item)
        }

        // 항목 클릭시 링크를 람다로 Activity에 전달
        private fun setItemClick(item: MovieResult.ItemResult) {
            itemView.setOnClickListener {
                listener(item.link)
            }
        }

    }

    inner class ProgressViewHolder(private val binding: ItemProgressbarBinding) :
        RecyclerView.ViewHolder(binding.root) {

            // progressBar 컨트롤
            fun bind() {
                if(isLast) {
                    binding.progressCircular.visibility = View.GONE
                } else {
                    binding.progressCircular.visibility = View.VISIBLE
                }
            }

        }


    private object MovieDiffUtil : DiffUtil.ItemCallback<MovieResult.ItemResult>() {

        override fun areItemsTheSame(
            oldItem: MovieResult.ItemResult,
            newItem: MovieResult.ItemResult
        ) =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: MovieResult.ItemResult,
            newItem: MovieResult.ItemResult
        ) =
            oldItem.hashCode() == newItem.hashCode() // isLast가 변화할때 리스트를 변경하기 위해서 hashCode로 검사

    }

}