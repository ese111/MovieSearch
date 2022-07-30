package com.example.moviesearch.data.dto


import com.example.moviesearch.data.model.MovieResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDTO(
    @SerialName("display")
    val display: Int = 0,
    @SerialName("items")
    val items: List<Item> = emptyList(),
    @SerialName("lastBuildDate")
    val lastBuildDate: String = "",
    @SerialName("start")
    val start: Int = 0,
    @SerialName("total")
    val total: Int = 0
) {
    @Serializable
    data class Item(
        @SerialName("actor")
        val actor: String = "",
        @SerialName("director")
        val director: String = "",
        @SerialName("image")
        val image: String = "",
        @SerialName("link")
        val link: String = "",
        @SerialName("pubDate")
        val pubDate: String = "",
        @SerialName("subtitle")
        val subtitle: String = "",
        @SerialName("title")
        val title: String = "",
        @SerialName("userRating")
        val userRating: String = ""
    )
}

fun MovieDTO.toMovieResult(): MovieResult {
    val items = items.map { it.toItemResult() }
    return MovieResult(display, items, lastBuildDate, start, total)
}

fun MovieDTO.Item.toItemResult(): MovieResult.ItemResult {
    return MovieResult.ItemResult(
        actor,
        director,
        image,
        link,
        pubDate,
        subtitle,
        title,
        userRating
    )
}
