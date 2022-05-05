package com.imzqqq.app.core.utils

import android.os.SystemClock
import android.view.View
import timber.log.Timber
import java.util.WeakHashMap

/**
 * Simple Debounced OnClickListener
 * Safe to use in different views
 */
class DebouncedClickListener(
        val original: View.OnClickListener,
        private val minimumInterval: Long = 400
) : View.OnClickListener {
    private val lastClickMap = WeakHashMap<View, Long>()

    override fun onClick(v: View) {
        val previousClickTimestamp = lastClickMap[v] ?: 0
        val currentTimestamp = SystemClock.elapsedRealtime()
        lastClickMap[v] = currentTimestamp

        if (currentTimestamp > previousClickTimestamp + minimumInterval) {
            original.onClick(v)
        } else {
            Timber.v("Debounced click!")
        }
    }
}
