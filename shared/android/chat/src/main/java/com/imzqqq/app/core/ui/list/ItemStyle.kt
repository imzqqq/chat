package com.imzqqq.app.core.ui.list

import android.graphics.Typeface

enum class ItemStyle {
    BIG_TEXT,
    NORMAL_TEXT,
    TITLE,
    SUBHEADER;

    fun toTypeFace(): Typeface {
        return if (this == TITLE) {
            Typeface.DEFAULT_BOLD
        } else {
            Typeface.DEFAULT
        }
    }

    fun toTextSize(): Float {
        return when (this) {
            BIG_TEXT    -> 18f
            NORMAL_TEXT -> 14f
            TITLE       -> 20f
            SUBHEADER   -> 16f
        }
    }
}
