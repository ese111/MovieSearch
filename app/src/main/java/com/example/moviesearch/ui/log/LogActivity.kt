package com.example.moviesearch.ui.log

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviesearch.databinding.ActivityLogBinding
import com.example.socarassignment.common.UiState
import com.example.socarassignment.common.logger
import com.example.socarassignment.common.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogActivity : AppCompatActivity() {

    private val binding: ActivityLogBinding by lazy {
        ActivityLogBinding.inflate(layoutInflater)
    }

    private val viewModel: LogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val logAdapter = LogAdapter {

        }

        binding.rvLog.apply {
            adapter = logAdapter
            layoutManager = GridLayoutManager(this.context, 5, GridLayoutManager.VERTICAL, false)
        }

        repeatOnStarted {
            viewModel.word.collect {
                when(it) {
                    is UiState.Error -> {
                        Toast.makeText(binding.root.context, viewModel.word.value._message, Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Success -> {
                        logAdapter.submitList(it.data.list)
                    }
                    else -> logger("log loading")
                }
            }
        }

    }
}