package com.example.moviesearch.ui.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesearch.data.model.Logs
import com.example.moviesearch.data.repository.LogRepository
import com.example.socarassignment.common.UiState
import com.example.socarassignment.common.logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(private val repository: LogRepository): ViewModel() {

    private val _words = MutableStateFlow<UiState<Logs>>(UiState.Loading)
    val word = _words.asStateFlow()

    init {
        getLog()
    }

    private fun getLog() {
        viewModelScope.launch {
            repository.getLog().catch {
                _words.value = UiState.Error("검색 기록을 불러오는데 실패하였습니다.")
            }.collect {
                _words.value = UiState.Success(it)
                it.list.forEach { d ->
                    logger("log : $d")
                }
            }
        }
    }
}