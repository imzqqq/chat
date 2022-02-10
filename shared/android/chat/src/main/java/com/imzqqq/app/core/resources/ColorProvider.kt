package com.imzqqq.app.core.resources

import android.content.Context
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.imzqqq.app.R
import com.imzqqq.app.features.themes.ThemeUtils
import javax.inject.Inject

class ColorProvider @Inject constructor(val context: Context) {

    @ColorInt
    fun getColor(@ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(context, colorRes)
        /*
        return when(colorRes) {
            R.color.riotx_accent -> getColorFromAttribute(R.attr.colorAccent)
            R.color.riotx_positive_accent -> getColorFromAttribute(R.attr.riotx_positive_accent)
            R.color.riotx_positive_accent_alpha12 -> getColorFromAttribute(R.attr.riotx_positive_accent_alpha12)
            else -> ContextCompat.getColor(context, colorRes)
        }
         */
    }

    /**
     * Translates color attributes to colors
     *
     * @param colorAttribute Color Attribute
     * @return Requested Color
     */
    @ColorInt
    fun getColorFromAttribute(@AttrRes colorAttribute: Int): Int {
        return ThemeUtils.getColor(context, colorAttribute)
    }
}
