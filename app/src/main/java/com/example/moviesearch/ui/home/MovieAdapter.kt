package com.example.moviesearch.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearch.data.model.MovieResult
import com.example.moviesearch.databinding.ItemMovieBinding

class MovieAdapter: ListAdapter<MovieResult.ItemResult, MovieAdapter.MovieViewHolder>(MovieDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MovieViewHolder(private val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MovieResult.ItemResult) {
            binding.item = item
        }
    }

    private object MovieDiffUtil: DiffUtil.ItemCallback<MovieResult.ItemResult>() {

        override fun areItemsTheSame(oldItem: MovieResult.ItemResult, newItem: MovieResult.ItemResult) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: MovieResult.ItemResult, newItem: MovieResult.ItemResult) =
            oldItem.title == newItem.title

    }

}