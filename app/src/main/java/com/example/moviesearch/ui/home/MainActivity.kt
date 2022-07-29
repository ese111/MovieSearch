package com.example.moviesearch.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearch.databinding.ActivityMainBinding
import com.example.moviesearch.ui.web.WebActivity
import com.example.socarassignment.common.UiState
import com.example.socarassignment.common.logger
import com.example.socarassignment.common.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MovieViewModel by viewModels()

    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setRecyclerViewAdapter()
        setSearchButton()
        setSearchLog()
        setResultObserver()
        setRecyclerViewScrollListener()
    }

    private fun setRecyclerViewScrollListener() {
        binding.rvMovieList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)
                if (lastVisibleItemPosition == itemTotalCount) {
                    viewModel.setMovieResult()
                }
            }

        })
    }

    private fun setSearchButton() {
        binding.btnSearch.setOnClickListener {
//            val name = binding.tiSearch.text.toString()
            viewModel.setMovieResult()
        }
    }

    private fun setResultObserver() {
        repeatOnStarted {
            viewModel.movieResult.collect {
                when(it) {
                    is UiState.Success -> {
                        if(it.data.items.isNotEmpty()) {
                            movieAdapter.submitList(it.data.items)
                        }
                    }
                    is UiState.Error -> {
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        logger("Loading")
                    }
                }
            }
        }
    }

    private fun setSearchLog() {
        binding.btnLog.setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            startActivity(intent)
        }
    }
    private fun setRecyclerViewAdapter() {
        binding.rvMovieList.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

}