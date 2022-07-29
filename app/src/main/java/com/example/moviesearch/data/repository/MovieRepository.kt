package com.example.moviesearch.data.repository

import com.example.moviesearch.data.datasource.MovieDataSource
import com.example.moviesearch.data.dto.toMovieResult
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(private val dataSource: MovieDataSource) {

    fun getMovieResult(name: String, start: Int) = flow {
        dataSource.getMovieResult(name, start).collect {
            emit(it.toMovieResult())
        }
    }

}