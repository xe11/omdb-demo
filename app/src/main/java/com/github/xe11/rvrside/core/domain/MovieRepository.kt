package com.github.xe11.rvrside.core.domain

import androidx.paging.Pager

internal interface MovieRepository {

    suspend fun searchMovies(
        query: String,
        page: Int = 1,
    ): Result<List<Movie>>

    suspend fun searchMoviesPaged(
        query: String,
    ): Pager<Int, Movie>

    suspend fun getMovieDetails(id: ImdbId): Result<MovieDetails>
}
