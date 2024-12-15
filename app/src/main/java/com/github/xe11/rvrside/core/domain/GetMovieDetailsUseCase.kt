package com.github.xe11.rvrside.core.domain

import javax.inject.Inject

internal class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository,
) {

    suspend fun execute(imdbId: ImdbId): Result<MovieDetails> = repository.getMovieDetails(imdbId)
}
