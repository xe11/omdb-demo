package com.github.xe11.rvrside.details.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.xe11.rvrside.details.presentation.MovieDetailsViewModel
import javax.inject.Inject

internal class MovieDetailsViewModelFactory @Inject constructor(
    private val viewModel: MovieDetailsViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return viewModel as T
    }
}
