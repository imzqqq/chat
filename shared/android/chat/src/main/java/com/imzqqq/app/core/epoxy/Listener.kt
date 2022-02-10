package com.imzqqq.app.core.epoxy

import android.view.View
import com.imzqqq.app.core.utils.DebouncedClickListener

/**
 * View.OnClickListener lambda
 */
typealias ClickListener = (View) -> Unit

fun View.onClick(listener: ClickListener?) {
    if (listener == null) {
        setOnClickListener(null)
    } else {
        setOnClickListener(DebouncedClickListener(listener))
    }
}

/**
 * Simple Text listener lambda
 */
typealias TextListener = (String) -> Unit
