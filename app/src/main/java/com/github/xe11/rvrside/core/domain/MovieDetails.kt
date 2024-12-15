package com.github.xe11.rvrside.core.domain

data class MovieDetails(
    val title: String,
    val year: String,
    val genre: String? = null,
    val director: String? = null,
    val actors: String? = null,
    val plot: String? = null,
    val imdbRating: String? = null,
    val id: ImdbId,
    val type: String,
    val posterUrl: String,
)
