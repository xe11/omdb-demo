package com.github.xe11.rvrside.search.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.xe11.rvrside.databinding.ActivityMovieListBinding
import com.github.xe11.rvrside.details.ui.MovieDetailsFragment
import com.github.xe11.rvrside.search.di.SearchScreenComponent
import com.github.xe11.rvrside.search.di.SearchViewModelFactory
import com.github.xe11.rvrside.search.presentation.MovieUi
import com.github.xe11.rvrside.search.presentation.SearchViewModel
import com.github.xe11.rvrside.utils.ui.collectLifecycleAware
import com.github.xe11.rvrside.utils.ui.scrollToCenter
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

internal class SearchActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: SearchViewModelFactory
    private val viewModel by viewModels<SearchViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = initView()
        inject()
        setupEdgeToEdge(binding)

        val adapter: MovieItemsAdapter = initMovieItemsAdapter(binding)

        bindViewModel(adapter, binding)

        binding.searchInput.setupClearOnCompoundEndClick()
    }

    private fun bindViewModel(
        adapter: MovieItemsAdapter,
        binding: ActivityMovieListBinding,
    ) {
        viewModel.moviesPaged.collectLifecycleAware(this) { moviesPagingData ->
            adapter.submitData(moviesPagingData)
        }

        viewModel.openDetailsScreen
            .receiveAsFlow()
            .collectLifecycleAware(this) {
                openDetailsView()
            }

        binding.searchInput.addTextChangedListener { searchQuery ->
            viewModel.onSearchQueryChanged(searchQuery.toString())
        }
    }

    private fun initView(): ActivityMovieListBinding {
        val binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupImeActions(binding.searchInput)

        return binding
    }

    private fun setupImeActions(editText: EditText) {
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        editText.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                inputManager.hideSoftInputFromWindow(view.windowToken, 0)

                true
            } else {
                false
            }
        }
    }

    private fun initMovieItemsAdapter(binding: ActivityMovieListBinding): MovieItemsAdapter {
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.moviesList)

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val onItemSelected: (Int, MovieUi) -> Unit = { position: Int, movie: MovieUi ->
            viewModel.onItemSelected(movie)
            binding.moviesList.scrollToCenter(position)
        }

        val adapter = MovieItemsAdapter(layoutInflater, onItemSelected)

        binding.moviesList.setLayoutManager(layoutManager)
        binding.moviesList.adapter = adapter.withLoadStateFooter()

        return adapter
    }

    private fun setupEdgeToEdge(binding: ActivityMovieListBinding) {
        enableEdgeToEdge()
        setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left
                rightMargin = insets.right
                bottomMargin = insets.bottom // TODO [Alexei Laban]: add filler in the ListView
                topMargin = insets.top
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun openDetailsView() {
        val bottomSheet = MovieDetailsFragment()
        bottomSheet.show(supportFragmentManager, "MovieDetailsFragment")
    }

    private fun inject() {
        SearchScreenComponent
            .get(this)
            .inject(this)
    }
}

// Not a customizable solution. It's better to refactor it in future.
@SuppressLint("ClickableViewAccessibility")
private fun EditText.setupClearOnCompoundEndClick() {
    setOnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            val drawableEnd = this.compoundDrawables[2] // Right drawable
            if (drawableEnd != null) {
                val touchX = event.rawX
                val drawableStartX = this.right - this.paddingEnd - drawableEnd.intrinsicWidth
                if (touchX >= drawableStartX) {
                    this.text?.clear()
                    return@setOnTouchListener true
                }
            }
        }
        false
    }
}
