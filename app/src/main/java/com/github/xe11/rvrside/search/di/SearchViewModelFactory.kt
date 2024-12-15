package com.github.xe11.rvrside.search.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.xe11.rvrside.search.presentation.SearchViewModel
import javax.inject.Inject

internal class SearchViewModelFactory @Inject constructor(
    private val viewModel: SearchViewModel,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return viewModel as T
    }
}
