package com.imzqqq.app.core.platform

import android.text.Editable
import android.text.TextWatcher

/**
 * TextWatcher with default no op implementation
 */
open class SimpleTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        // No op
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        // No op
    }

    override fun afterTextChanged(s: Editable) {
        // No op
    }
}
