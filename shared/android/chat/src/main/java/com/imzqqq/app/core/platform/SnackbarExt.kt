package com.imzqqq.app.core.platform

import android.view.View
import com.google.android.material.snackbar.Snackbar

private const val MIN_SNACKBAR_DURATION = 2000
private const val MAX_SNACKBAR_DURATION = 8000
private const val DURATION_PER_LETTER = 50

fun View.showOptimizedSnackbar(message: String) {
    Snackbar.make(this, message, getDuration(message)).show()
}

private fun getDuration(message: String): Int {
    return (message.length * DURATION_PER_LETTER).coerceIn(MIN_SNACKBAR_DURATION, MAX_SNACKBAR_DURATION)
}
