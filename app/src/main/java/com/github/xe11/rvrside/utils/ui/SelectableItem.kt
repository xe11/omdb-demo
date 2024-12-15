package com.github.xe11.rvrside.utils.ui

data class SelectableItem<T : Any>(
    val selected: Boolean,
    val data: T,
)
