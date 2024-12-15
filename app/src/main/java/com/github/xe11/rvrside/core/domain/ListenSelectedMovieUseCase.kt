package com.github.xe11.rvrside.core.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ListenSelectedMovieUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {

    fun execute(): Flow<ImdbId?> = searchRepository.listenSelectedMovie()
}
