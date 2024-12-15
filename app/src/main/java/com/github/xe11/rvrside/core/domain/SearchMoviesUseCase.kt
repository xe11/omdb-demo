package com.github.xe11.rvrside.core.domain

import androidx.paging.Pager
import javax.inject.Inject

internal class SearchMoviesUseCase @Inject constructor(
    private val repository: MovieRepository,
) {

    suspend fun execute(query: String): Pager<Int, Movie> = repository.searchMoviesPaged(query)
}
