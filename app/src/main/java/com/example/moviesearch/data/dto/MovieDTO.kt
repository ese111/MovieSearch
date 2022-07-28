package com.example.moviesearch.data.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDTO(
    @SerialName("display")
    val display: Int?,
    @SerialName("items")
    val items: List<Item?>?,
    @SerialName("lastBuildDate")
    val lastBuildDate: String?,
    @SerialName("start")
    val start: Int?,
    @SerialName("total")
    val total: Int?
)