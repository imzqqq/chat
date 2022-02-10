package com.imzqqq.app.features.html

import android.os.Build
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.text.style.UnderlineSpan
import javax.inject.Inject

class SpanUtils @Inject constructor() {
    // Workaround for https://issuetracker.google.com/issues/188454876
    fun canUseTextFuture(charSequence: CharSequence): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            // On old devices, it works correctly
            return true
        }

        if (charSequence !is Spanned) {
            return true
        }

        return charSequence
                .getSpans(0, charSequence.length, Any::class.java)
                .all { it !is StrikethroughSpan && it !is UnderlineSpan }
    }
}
