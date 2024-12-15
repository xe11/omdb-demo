package com.github.xe11.rvrside.details.presentation

import com.github.xe11.rvrside.core.domain.MovieDetails

internal sealed interface MovieDetailsUiState {

    data object NoData : MovieDetailsUiState

    data object Loading : MovieDetailsUiState

    data object Error : MovieDetailsUiState

    data class Details(
        val details: MovieDetailsUi,
    ) : MovieDetailsUiState
}

internal data class MovieDetailsUi(
    val title: String,
    val year: String,
    val plot: String?,
    val posterUrl: String,
)

internal fun MovieDetails.toMovieDetailsUi(): MovieDetailsUi {
    return MovieDetailsUi(
        title = title,
        year = year,
        posterUrl = posterUrl,
        plot = plot,
    )
}
