package com.github.xe11.rvrside.core.data

import com.github.xe11.rvrside.core.domain.ImdbId
import com.github.xe11.rvrside.core.domain.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

internal class SearchRepositoryImpl @Inject constructor() : SearchRepository {

    private val selectedMovie = MutableStateFlow<ImdbId?>(null)

    override fun setSelectedMovie(imdbId: ImdbId?) {
        selectedMovie.value = imdbId
    }

    override fun listenSelectedMovie(): Flow<ImdbId?> =
        selectedMovie
}
