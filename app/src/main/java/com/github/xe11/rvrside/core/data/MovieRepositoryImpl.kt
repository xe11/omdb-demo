package com.github.xe11.rvrside.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.xe11.rvrside.core.domain.ImdbId
import com.github.xe11.rvrside.core.domain.Movie
import com.github.xe11.rvrside.core.domain.MovieDetails
import com.github.xe11.rvrside.core.domain.MovieRepository
import com.github.xe11.rvrside.utils.runSuspendCatching
import javax.inject.Inject

// TODO [Alexei Laban]: inject API Key from the App side
private const val API_KEY = "ef038a3e"
private const val API_SEARCH_PAGE_SIZE = 10

internal class MovieRepositoryImpl @Inject constructor(
    private val moviesApi: MoviesApi,
) : MovieRepository {

    private val pagingConfig = PagingConfig(
        pageSize = API_SEARCH_PAGE_SIZE,
    )

    override suspend fun searchMovies(
        query: String,
        page: Int,
    ): Result<List<Movie>> {
        return runSuspendCatching {
            moviesApi.searchMovies(
                apiKey = API_KEY,
                query = query,
                page = page,
            )
        }
            .map { searchResponse ->
                searchResponse.search
                    ?.map(MovieJson::toMovie)
                    ?: emptyList()
            }
    }

    override suspend fun searchMoviesPaged(query: String): Pager<Int, Movie> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { MoviePagingSource { page -> searchMovies(query, page) } }
        )
    }

    override suspend fun getMovieDetails(id: ImdbId): Result<MovieDetails> {
        return runSuspendCatching {
            moviesApi.getMovieDetails(API_KEY, id).toMovieDetails()
        }
    }
}

private fun MovieDetailsJson.toMovieDetails(): MovieDetails {
    return MovieDetails(
        title = title,
        year = year,
        genre = genre,
        director = director,
        actors = actors,
        plot = plot,
        imdbRating = imdbRating,
        id = imdbID,
        type = type,
        posterUrl = posterUrl,
    )
}

private fun MovieJson.toMovie(): Movie {
    return Movie(
        title = title,
        year = year,
        id = imdbID,
        type = type,
        posterUrl = posterUrl
    )
}

private class MoviePagingSource(
    private val loadPage: suspend (page: Int) -> Result<List<Movie>>,
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?: 1
            val movieListResult: Result<List<Movie>> = loadPage(currentPage)

            movieListResult.fold(
                onSuccess = { movieList ->
                    LoadResult.Page(
                        data = movieList,
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (movieList.isEmpty()) null else currentPage + 1,
                    )
                },
                onFailure = { error ->
                    LoadResult.Error(error)
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = null
}
