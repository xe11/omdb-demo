package com.github.xe11.rvrside.utils.ui

import android.widget.ImageView
import coil3.load
import coil3.request.error
import coil3.request.placeholder
import com.github.xe11.rvrside.R

fun ImageView.loadCoverImage(posterUrl: String) {
    this.load(posterUrl) {
        placeholder(R.drawable.ic_movie_placeholder)
        error(android.R.drawable.ic_dialog_alert)
    }
}
