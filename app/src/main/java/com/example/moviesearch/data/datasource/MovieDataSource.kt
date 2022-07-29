package com.example.moviesearch.data.datasource

import com.example.moviesearch.data.network.MovieApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDataSource @Inject constructor(private val api: MovieApi) {

    fun getMovieResult(name: String, start: Int) = flow {
        emit(api.getMovieSearch(name = name, start = start))
    }.flowOn(Dispatchers.IO)

}