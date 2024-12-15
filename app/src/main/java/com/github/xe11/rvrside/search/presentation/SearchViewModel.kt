package com.github.xe11.rvrside.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.github.xe11.rvrside.core.domain.Movie
import com.github.xe11.rvrside.core.domain.SearchMoviesUseCase
import com.github.xe11.rvrside.core.domain.SetSelectedMovieUseCase
import com.github.xe11.rvrside.utils.ui.SelectableItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

private val searchDebounceTimeout = 500.milliseconds

internal class SearchViewModel @Inject constructor(
    private val setSelectedMovie: SetSelectedMovieUseCase,
    private val searchMovies: SearchMoviesUseCase,
) : ViewModel() {

    private val searchQuery = MutableStateFlow<String>("")
    private val selectedItemState = MutableStateFlow<MovieUi?>(null)

    private val searchResults: Flow<PagingData<Movie>> = searchQuery
        .flatMapLatest { query ->
            flow {
                emit(flowOf(PagingData.empty<Movie>()))
                delay(searchDebounceTimeout)
                emit(searchMovies.execute(query).flow)
            }.flattenConcat()
        }
        .cachedIn(viewModelScope)

    val moviesPaged: Flow<PagingData<SelectableItem<MovieUi>>> =
        combine(
            searchResults,
            selectedItemState,
        ) { pagingData, selectedMovie -> pagingData to selectedMovie }
            .map { (pagingData, selectedMovie) ->
                pagingData.map { movie ->
                    val uiItem = movie.toMovieUi()

                    SelectableItem(
                        selected = movie.id == selectedMovie?.id,
                        data = uiItem,
                    )
                }
            }
            // TODO [Alexei Laban]: extract DispatchersProvider for tests
            .flowOn(Dispatchers.Default)
            .cachedIn(viewModelScope)

    private val _openDetailsScreen: Channel<Unit> = Channel(capacity = CONFLATED)
    val openDetailsScreen: ReceiveChannel<Unit> = _openDetailsScreen

    fun onSearchQueryChanged(query: String) {
        viewModelScope.launch {
            searchQuery.value = query
        }
    }

    fun onItemSelected(movie: MovieUi) {
        viewModelScope.launch {
            selectItem(movie)
            _openDetailsScreen.send(Unit)
        }
    }

    private fun selectItem(movie: MovieUi?) {
        selectedItemState.value = movie
        setSelectedMovie.execute(movie?.id)
    }
}
