package com.example.moviesearch.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearch.data.model.MovieResult
import com.example.moviesearch.databinding.ItemMovieBinding

class MovieAdapter(private val listener: (String) -> Unit) :
    ListAdapter<MovieResult.ItemResult, MovieAdapter.MovieViewHolder>(MovieDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MovieResult.ItemResult) {
            binding.item = item
            setItemClick(item)
        }

        private fun setItemClick(item: MovieResult.ItemResult) {
            itemView.setOnClickListener {
                listener(item.link)
            }
        }
    }

    private object MovieDiffUtil : DiffUtil.ItemCallback<MovieResult.ItemResult>() {

        override fun areItemsTheSame(
            oldItem: MovieResult.ItemResult,
            newItem: MovieResult.ItemResult
        ) =
            oldItem == newItem // isLast가 변화할때 리스트를 변경하기 위해서 hashCode로 검사

        override fun areContentsTheSame(
            oldItem: MovieResult.ItemResult,
            newItem: MovieResult.ItemResult
        ) =
            oldItem.title == newItem.title

    }

}