package com.github.xe11.rvrside.details.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.github.xe11.rvrside.databinding.FragmentMovieDetailsBinding
import com.github.xe11.rvrside.details.di.DetailsScreenComponent
import com.github.xe11.rvrside.details.di.MovieDetailsViewModelFactory
import com.github.xe11.rvrside.details.presentation.MovieDetailsUi
import com.github.xe11.rvrside.details.presentation.MovieDetailsUiState
import com.github.xe11.rvrside.details.presentation.MovieDetailsViewModel
import com.github.xe11.rvrside.utils.ui.collectLifecycleAware
import com.github.xe11.rvrside.utils.ui.loadCoverImage
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

internal class MovieDetailsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: MovieDetailsViewModelFactory
    private val viewModel by viewModels<MovieDetailsViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViewModel()
    }

    private fun bindViewModel() {
        viewModel.state.collectLifecycleAware(this) { state ->
            bindState(state)
        }
    }

    private fun bindState(state: MovieDetailsUiState) {
        when (state) {
            MovieDetailsUiState.Loading,
            MovieDetailsUiState.NoData,
                -> showLoading()

            MovieDetailsUiState.Error,
                -> showError()

            is MovieDetailsUiState.Details,
                -> bindDetails(state.details)
        }
    }

    private fun bindDetails(details: MovieDetailsUi) {
        showContent()
        binding.run {
            root.isVisible = true
            moviePoster.loadCoverImage(details.posterUrl)
            movieTitle.text = details.title
            productionYear.text = details.year
            plotSummary.text = details.plot
        }
    }

    private fun showError() {
        hideAll()
        binding.errorMessage.isVisible = true
    }

    private fun showContent() {
        hideAll()
        binding.movieContent.isVisible = true
    }

    private fun showLoading() {
        hideAll()
        binding.progressBar.isVisible = true
    }

    private fun hideAll() {
        binding.movieContent.isVisible = false
        binding.errorMessage.isVisible = false
        binding.progressBar.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun inject() {
        DetailsScreenComponent
            .get(requireContext())
            .inject(this)
    }
}
