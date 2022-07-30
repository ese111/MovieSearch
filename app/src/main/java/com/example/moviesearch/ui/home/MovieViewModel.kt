package com.example.moviesearch.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesearch.common.UiState
import com.example.moviesearch.data.model.MovieResult
import com.example.moviesearch.data.repository.LogRepository
import com.example.moviesearch.data.repository.MovieRepository
import com.example.socarassignment.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieRepository: MovieRepository, private val logRepository: LogRepository) : ViewModel() {

    private val _movieResult =
        MutableStateFlow<UiState<List<MovieResult.ItemResult>>>(UiState.Loading)
    val movieResult = _movieResult.asStateFlow()

    // 검색어
    var word = ""

    private var paging = 1

    fun setLog(word: String) {
        viewModelScope.launch {
            logRepository.setLog(word)
        }
    }

    fun setMovieResult() {
        viewModelScope.launch {

            // 마지막 페이지 체크
            if (total == paging || total == 1) {
                return@launch
            }

            movieRepository.getMovieResult(word, paging).catch {
                _movieResult.value = UiState.Error("네트워크 연결 실패")
            }.collect {
                // 검색결과를 현재 결과에 추가
                val list = mutableListOf<MovieResult.ItemResult>()
                _movieResult.value._data?.let { items -> list.addAll(items) }
                list.addAll(it.items)

                _movieResult.value = UiState.Success(list)
            }

            paging += 10 // 페이지 +10
            if (total < paging) {
                paging = total
            }
        }
    }

    fun setFirstMovieResult() {
        paging = 1
        viewModelScope.launch {
            movieRepository.getMovieResult(name, paging).catch {
                _movieResult.value = UiState.Error("네트워크 연결 실패")
            }.collect {

                total = it.total // 총 데이터
                _movieResult.value = UiState.Success(it.items)
            }
            paging += 10
        }
    }

}