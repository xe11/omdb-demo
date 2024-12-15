package com.github.xe11.rvrside.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.unit.dp
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.xe11.rvrside.databinding.ItemMovieBinding
import com.github.xe11.rvrside.search.presentation.MovieUi
import com.github.xe11.rvrside.utils.ui.CommonLoadStateAdapter
import com.github.xe11.rvrside.utils.ui.SelectableItem
import com.github.xe11.rvrside.utils.ui.loadCoverImage
import com.github.xe11.rvrside.utils.ui.toPx

internal class MovieItemsAdapter(
    private val inflater: LayoutInflater,
    private val onItemClicked: (position: Int, item: MovieUi) -> Unit,
) : PagingDataAdapter<SelectableItem<MovieUi>, MovieItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MovieItemViewHolder {
        val binding = ItemMovieBinding.inflate(inflater, parent, false)

        return MovieItemViewHolder(
            binding,
            onItemClicked,
        )
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        getItem(position)?.let { movieItem -> holder.bind(movieItem) }
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    class DiffCallback : DiffUtil.ItemCallback<SelectableItem<MovieUi>>() {
        override fun areItemsTheSame(oldItem: SelectableItem<MovieUi>, newItem: SelectableItem<MovieUi>): Boolean {
            return oldItem.data.id == newItem.data.id
        }

        override fun areContentsTheSame(oldItem: SelectableItem<MovieUi>, newItem: SelectableItem<MovieUi>): Boolean {
            return oldItem == newItem
        }
    }
}

internal class MovieItemViewHolder(
    private val movieBinding: ItemMovieBinding,
    private val onItemClicked: (position: Int, item: MovieUi) -> Unit,
) : RecyclerView.ViewHolder(movieBinding.root) {

    fun bind(item: SelectableItem<MovieUi>) {
        movieBinding.run {
            val movie = item.data

            name.text = movie.title
            subtitle.text = movie.year
            coverImage.loadCoverImage(movie.posterUrl)

            root.setOnClickListener {
                onItemClicked(bindingAdapterPosition, movie)
            }

            val elevation = (if (item.selected) 24.dp else 0.dp).toPx(root.context)
            root.elevation = elevation
        }
    }
}

internal fun MovieItemsAdapter.withLoadStateFooter(): ConcatAdapter {
    return this.withLoadStateFooter(
        footer = CommonLoadStateAdapter { retry() }
    )
}
