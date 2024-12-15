package com.github.xe11.rvrside.search.presentation

import com.github.xe11.rvrside.core.domain.Movie

internal data class MovieUi(
    val id: String,
    val title: String,
    val year: String,
    val posterUrl: String,
)

internal fun Movie.toMovieUi(): MovieUi {
    return MovieUi(
        id = id,
        title = title,
        year = year,
        posterUrl = posterUrl
    )
}
