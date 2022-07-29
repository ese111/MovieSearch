package com.example.moviesearch.data.datasource

import com.example.moviesearch.common.DataStoreLoader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogDataSource @Inject constructor(private val loader: DataStoreLoader) {

    suspend fun setLog(word: String) {
        loader.setLog(word)
    }

    fun getLog() = loader.getLog()

}