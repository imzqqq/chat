package com.imzqqq.app.features.themes

import android.content.Context
import javax.inject.Inject

/**
 * Injectable class to encapsulate ThemeUtils call...
 */
class ThemeProvider @Inject constructor(
        private val context: Context
) {
    fun isLightTheme() = ThemeUtils.isLightTheme(context)
    fun isBlackTheme() = ThemeUtils.isBlackTheme(context)
}
