package com.imzqqq.app.features.home.room.detail.timeline.helper

import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import kotlin.math.roundToInt

@ActivityScoped
class TimelineMediaSizeProvider @Inject constructor() {

    var recyclerView: RecyclerView? = null
    private var cachedSize: Pair<Int, Int>? = null

    fun getMaxSize(): Pair<Int, Int> {
        return cachedSize ?: computeMaxSize().also { cachedSize = it }
    }

    private fun computeMaxSize(): Pair<Int, Int> {
        val width = recyclerView?.width ?: 0
        val height = recyclerView?.height ?: 0
        val maxImageWidth: Int
        val maxImageHeight: Int
        // landscape / portrait
        if (width < height) {
            maxImageWidth = (width * 0.7f).roundToInt()
            maxImageHeight = (height * 0.5f).roundToInt()
        } else {
            maxImageWidth = (width * 0.5f).roundToInt()
            maxImageHeight = (height * 0.7f).roundToInt()
        }
        return Pair(maxImageWidth, maxImageHeight)
    }
}
