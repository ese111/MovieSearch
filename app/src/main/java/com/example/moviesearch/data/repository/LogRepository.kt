package com.example.moviesearch.data.repository

import com.example.moviesearch.data.datasource.LogDataSource
import com.example.moviesearch.data.model.Logs
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogRepository @Inject constructor(private val dataSource: LogDataSource) {

    suspend fun setLog(word: String) {
        dataSource.setLog(word)
    }

    fun getLog() = flow {
        dataSource.getLog().collect {
            emit(Logs(it))
        }
    }
}