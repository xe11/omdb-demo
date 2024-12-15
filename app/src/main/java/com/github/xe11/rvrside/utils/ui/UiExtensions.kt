package com.github.xe11.rvrside.utils.ui

import android.content.Context
import androidx.compose.ui.unit.Dp

fun Dp.toPx(context: Context): Float {
    return this.value * context.resources.displayMetrics.density
}
