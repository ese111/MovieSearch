package com.example.moviesearch.ui.log

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviesearch.common.UiState
import com.example.moviesearch.common.logger
import com.example.moviesearch.common.repeatOnStarted
import com.example.moviesearch.databinding.ActivityLogBinding
import com.example.moviesearch.ui.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogActivity : AppCompatActivity() {

    private val binding: ActivityLogBinding by lazy {
        ActivityLogBinding.inflate(layoutInflater)
    }

    private val viewModel: LogViewModel by viewModels()

    private val logAdapter: LogAdapter by lazy {
        LogAdapter { log ->
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("log", log)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setAdapter()
        setWordObserve()
    }

    private fun setAdapter() {
        binding.rvLog.apply {
            adapter = logAdapter
            layoutManager = GridLayoutManager(this.context, 5, GridLayoutManager.VERTICAL, false)
        }

    }

    // 검색 기록 뷰
    private fun setWordObserve() {
        repeatOnStarted {
            viewModel.word.collect {
                when (it) {
                    is UiState.Error -> {
                        Toast.makeText(
                            binding.root.context,
                            viewModel.word.value._message,
                            Toast.LENGTH_SHORT
                        ).show()
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