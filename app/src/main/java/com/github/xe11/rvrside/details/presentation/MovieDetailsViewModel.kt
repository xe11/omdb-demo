package com.github.xe11.rvrside.details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.xe11.rvrside.core.domain.GetMovieDetailsUseCase
import com.github.xe11.rvrside.core.domain.ImdbId
import com.github.xe11.rvrside.core.domain.ListenSelectedMovieUseCase
import com.github.xe11.rvrside.details.presentation.MovieDetailsUiState.Details
import com.github.xe11.rvrside.details.presentation.MovieDetailsUiState.Error
import com.github.xe11.rvrside.details.presentation.MovieDetailsUiState.Loading
import com.github.xe11.rvrside.details.presentation.MovieDetailsUiState.NoData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

internal class MovieDetailsViewModel @Inject constructor(
    private val listenSelectedMovie: ListenSelectedMovieUseCase,
    private val getMovieDetails: GetMovieDetailsUseCase,
) : ViewModel() {

    val state: Flow<MovieDetailsUiState> =
        listenSelectedMovie.execute()
            .flatMapLatest(::loadMovieDetails)
            .stateIn(viewModelScope, Eagerly, NoData)

    private fun loadMovieDetails(imdbId: ImdbId?): Flow<MovieDetailsUiState> = flow {
        if (imdbId == null) {
            emit(NoData)
        } else {
            emit(Loading)
            getMovieDetails.execute(imdbId)
                .onFailure {
                    emit(Error)
                }
                .onSuccess { movieDetails ->
                    val detailsUi = Details(movieDetails.toMovieDetailsUi())
                    emit(detailsUi)
                }
        }
    }
}
