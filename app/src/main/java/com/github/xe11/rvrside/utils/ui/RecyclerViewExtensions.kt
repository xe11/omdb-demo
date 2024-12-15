package com.github.xe11.rvrside.utils.ui

import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.scrollToCenter(position: Int) {
    val smoothScroller = object : LinearSmoothScroller(this.context) {
        override fun getVerticalSnapPreference(): Int = SNAP_TO_START
        override fun getHorizontalSnapPreference(): Int = SNAP_TO_START

        override fun calculateDtToFit(
            viewStart: Int,
            viewEnd: Int,
            boxStart: Int,
            boxEnd: Int,
            snapPreference: Int,
        ): Int {
            val dt = super.calculateDtToFit(viewStart, viewEnd, boxStart, boxEnd, snapPreference)
            return dt + (boxEnd - boxStart) / 2 - (viewEnd - viewStart) / 2
        }
    }

    smoothScroller.targetPosition = position
    this.layoutManager?.startSmoothScroll(smoothScroller)
}
