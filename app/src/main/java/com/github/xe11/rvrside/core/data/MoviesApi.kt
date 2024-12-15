package com.github.xe11.rvrside.core.data

import com.github.xe11.rvrside.core.domain.ImdbId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

internal interface MoviesApi {

    @GET("/")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") query: String,
        @Query("page") page: Int? = 1,
    ): SearchResponse

    @GET("/")
    suspend fun getMovieDetails(
        @Query("apikey") apiKey: String,
        @Query("i") imdbID: ImdbId,
    ): MovieDetailsJson
}

@Serializable
data class SearchResponse(
    @SerialName("Search") val search: List<MovieJson>? = null,
    @SerialName("totalResults") val totalResults: String? = null,
    @SerialName("Response") val response: String
)

@Serializable
data class MovieJson(
    @SerialName("Title") val title: String,
    @SerialName("Year") val year: String,
    @SerialName("imdbID") val imdbID: String,
    @SerialName("Type") val type: String,
    @SerialName("Poster") val posterUrl: String,
)

@Serializable
data class MovieDetailsJson(
    @SerialName("Title") val title: String,
    @SerialName("Year") val year: String,
    @SerialName("Genre") val genre: String? = null,
    @SerialName("Director") val director: String? = null,
    @SerialName("Actors") val actors: String? = null,
    @SerialName("Plot") val plot: String? = null,
    @SerialName("imdbRating") val imdbRating: String? = null,
    @SerialName("imdbID") val imdbID: String,
    @SerialName("Type") val type: String,
    @SerialName("Poster") val posterUrl: String,
)
