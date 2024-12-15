package com.github.xe11.rvrside.core.domain

import javax.inject.Inject

internal class SetSelectedMovieUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {

    fun execute(imdbId: ImdbId?): Unit = searchRepository.setSelectedMovie(imdbId)
}
