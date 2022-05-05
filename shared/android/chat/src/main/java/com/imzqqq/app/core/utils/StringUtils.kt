package com.imzqqq.app.core.utils

import java.util.Locale

// String.capitalize is now deprecated
fun String.safeCapitalize(locale: Locale): String {
    return replaceFirstChar { char ->
        if (char.isLowerCase()) {
            char.titlecase(locale)
        } else {
            char.toString()
        }
    }
}
