package com.example.moviesearch.data.model


data class MovieResult(
    val display: Int,
    val items: List<ItemResult>,
    val lastBuildDate: String,
    val start: Int,
    val total: Int
) {
    data class ItemResult(
        val actor: String,
        val director: String,
        val image: String,
        val link: String,
        val pubDate: String,
        val subtitle: String,
        val title: String,
        val userRating: String
    )
}
