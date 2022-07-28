package com.example.moviesearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesearch.data.model.MovieResult
import com.example.moviesearch.data.repository.MovieRepository
import com.example.socarassignment.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository): ViewModel() {

    private val _movieResult = MutableStateFlow<UiState<MovieResult>>(UiState.Loading)
    val movieResult = _movieResult.asStateFlow()

    private var paging = 1

    fun getMovieResult(name: String) {
        viewModelScope.launch {
            repository.getMovieResult(name, paging).catch {
                _movieResult.value = UiState.Error("네트워크 연결 실패")
            }.collect {
                _movieResult.value = UiState.Success(it)
            }
        }
    }
}