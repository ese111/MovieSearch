package com.example.moviesearch.data.network

import com.example.moviesearch.data.dto.MovieDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("search/movie.json")
    suspend fun getMovieSearch(
        @Query("query") name: String,
        @Query("display") display: Int = 10,
        @Query("start") start: Int
    ): MovieDTO

}