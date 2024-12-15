package com.github.xe11.rvrside.core.domain

data class Movie(
    val title: String,
    val year: String,
    val id: ImdbId,
    val type: String, // TODO [Alexei Laban]: enum:: movie, series, episode
    val posterUrl: String,
)
