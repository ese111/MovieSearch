package com.example.moviesearch.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearch.databinding.ActivityMainBinding
import com.example.moviesearch.ui.log.LogActivity
import com.example.socarassignment.common.UiState
import com.example.socarassignment.common.logger
import com.example.socarassignment.common.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MovieViewModel by viewModels()

    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter { link ->
            CustomTabsIntent.Builder().build().launchUrl(this, Uri.parse(link))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setRecyclerViewAdapter()
        setSearchButton()
        setSearchLog()
        setResultObserver()
        setRecyclerViewScrollListener()
        setViewModel()
    }

    private fun setRecyclerViewScrollListener() {
        binding.rvMovieList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() // 화면에 보이는 마지막 아이템의 position
                val itemTotalCount = recyclerView.adapter!!.itemCount - 5 //

                // 스크롤이 끝에 도달했는지 확인
                if (lastVisibleItemPosition == itemTotalCount) {
                    viewModel.setMovieResult()
                }
            }

        })
    }

    private fun setSearchButton() {
        binding.btnSearch.setOnClickListener {
            viewModel.name = binding.tiSearch.text.toString()
            viewModel.setLog(binding.tiSearch.text.toString())
            viewModel.setFirstMovieResult()
        }
    }

    private fun setResultObserver() {
        repeatOnStarted {
            viewModel.movieResult.collect {
                when (it) {
                    is UiState.Success -> {
                        if (it.data.isNotEmpty()) {
                            movieAdapter.submitList(it.data)
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
            val intent = Intent(this, LogActivity::class.java)
            startForResult.launch(intent)
        }
    }

    // adapter setting
    private fun setRecyclerViewAdapter() {
        binding.rvMovieList.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

}