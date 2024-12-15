package com.github.xe11.rvrside.core.domain

import kotlinx.coroutines.flow.Flow

internal interface SearchRepository {

    fun setSelectedMovie(imdbId: ImdbId?)

    fun listenSelectedMovie(): Flow<ImdbId?>
}
