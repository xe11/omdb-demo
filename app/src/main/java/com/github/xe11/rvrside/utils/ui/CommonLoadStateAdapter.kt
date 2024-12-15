package com.github.xe11.rvrside.utils.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.xe11.rvrside.databinding.ItemLoadStateFooterBinding

internal class CommonLoadStateViewHolder(
    private val binding: ItemLoadStateFooterBinding,
    retry: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMessage.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMessage.isVisible = loadState is LoadState.Error
    }

    companion object {

        fun create(parent: ViewGroup, retry: () -> Unit): CommonLoadStateViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemLoadStateFooterBinding.inflate(layoutInflater, parent, false)

            return CommonLoadStateViewHolder(binding, retry)
        }
    }
}

internal class CommonLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<CommonLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: CommonLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): CommonLoadStateViewHolder {
        return CommonLoadStateViewHolder.create(parent, retry)
    }
}
