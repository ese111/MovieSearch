package com.example.moviesearch.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearch.common.UiState
import com.example.moviesearch.common.logger
import com.example.moviesearch.common.repeatOnStarted
import com.example.moviesearch.data.model.MovieResult
import com.example.moviesearch.databinding.ActivityMainBinding
import com.example.moviesearch.ui.log.LogActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // MainActivityBinding
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // viewModel
    private val viewModel: MovieViewModel by viewModels()

    // RecyclerView Adapter
    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter { link ->
            CustomTabsIntent.Builder().build().launchUrl(this, Uri.parse(link))
        }
    }

    // 검색 기록 화면 이동 후 선택한 검색 단어를 받아서 Api로 검색
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val word = result.data?.getStringExtra("log")
                binding.tiSearch.setText(word)
                viewModel.setFirstMovieResult()
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
        setIsLastObserve()
    }

    // TextInputEditText과 viewModel의 양방향 데이터 바인딩을 위한 viewModel 설정
    private fun setViewModel() {
        binding.vm = viewModel
    }

    // RecyclerView의 스크롤 감지
    private fun setRecyclerViewScrollListener() {
        binding.rvMovieList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() // 화면에 보이는 마지막 아이템의 position

                val itemTotalCount = recyclerView.adapter!!.itemCount - 1 // RecyclerView Item의 개수
                // 스크롤이 끝에 도달했는지 확인
                if (lastVisibleItemPosition == itemTotalCount && itemTotalCount >= 9) {
                    // 다음 페이지 불러오기
                    viewModel.setMovieResult()
                }
            }
        })
    }

    // 검색 버튼 클릭
    private fun setSearchButton() {
        binding.btnSearch.setOnClickListener {
            viewModel.setLog(binding.tiSearch.text.toString()) // 최근 검색에 단어 저장
            viewModel.setFirstMovieResult() // 첫페이지 불러오기

            // 키보드 내리기
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.tiSearch.windowToken, 0)
        }
    }

    // ViewModel의 StateFlow Observe
    private fun setResultObserver() {
        repeatOnStarted {
            viewModel.movieResult.collect {
                when (it) {
                    is UiState.Success -> {
                        // 성공하면 기존의 결과에 새로운 결과를 추가한다.
                        val list = mutableListOf<MovieResult.ItemResult>()
                        viewModel.movieResult.value._data?.let { result -> list.addAll(result) }
                        movieAdapter.submitList(list)
                    }
                    is UiState.Error -> {
                        // 실패시 toastMessage 출력
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        logger("Loading")
                    }
                }
            }
        }
    }

    // 검색 기록 화면으로 이동
    private fun setSearchLog() {
        binding.btnLog.setOnClickListener {
            val intent = Intent(this, LogActivity::class.java)
            startForResult.launch(intent)
        }
    }

    // 마지막 페이지인지 Observe
    private fun setIsLastObserve() {
        repeatOnStarted {
            viewModel.isLast.collect {
                // 마지막 페이지이면 어뎁터에 알려주어 progressBar를 감춘다
                val list = mutableListOf<MovieResult.ItemResult>()
                viewModel.movieResult.value._data?.let { data -> list.addAll(data) }
                movieAdapter.isLast = it
            }
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