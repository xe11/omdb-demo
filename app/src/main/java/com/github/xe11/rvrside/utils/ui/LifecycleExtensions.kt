package com.github.xe11.rvrside.utils.ui

import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> Flow<T>.collectLifecycleAware(
    lifecycleOwner: LifecycleOwner,
    action: suspend (value: T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        this@collectLifecycleAware
            .flowWithLifecycle(lifecycleOwner.lifecycle, STARTED)
            .collectLatest(action)
    }
}
