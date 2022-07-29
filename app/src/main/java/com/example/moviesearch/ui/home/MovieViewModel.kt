package com.example.moviesearch.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    var name = ""

    private var paging = 1

    fun setLog(word: String) {
        viewModelScope.launch {
            logRepository.setLog(word)
        }
    }

    fun setMovieResult() {
        viewModelScope.launch {
            movieRepository.getMovieResult(name, paging).catch {
                _movieResult.value = UiState.Error("네트워크 연결 실패")
            }.collect {
                if (it.total <= paging) {
                    _movieResult.value = UiState.Error("검색 결과가 없습니다.")
                    return@collect
                }
                val list = mutableListOf<MovieResult.ItemResult>()
                _movieResult.value._data?.let { items -> list.addAll(items) }
                list.addAll(it.items)
                _movieResult.value = UiState.Success(list)
            }
            paging += 10
        }
    }

    fun setFirstMovieResult() {
        paging = 1
        viewModelScope.launch {
            movieRepository.getMovieResult(name, paging).catch {
                _movieResult.value = UiState.Error("네트워크 연결 실패")
            }.collect {
                if (it.total <= paging) {
                    _movieResult.value = UiState.Error("검색 결과가 없습니다.")
                    return@collect
                }
                val list = mutableListOf<MovieResult.ItemResult>()
                _movieResult.value._data?.let { items -> list.addAll(items) }
                list.addAll(it.items)
                _movieResult.value = UiState.Success(list)
            }
            paging += 10
        }
    }

}