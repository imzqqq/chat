package com.imzqqq.app.features.autocomplete.command

import android.text.Spannable
import com.otaliastudios.autocomplete.AutocompletePolicy
import javax.inject.Inject

class CommandAutocompletePolicy @Inject constructor() : AutocompletePolicy {

    var enabled: Boolean = true

    override fun getQuery(text: Spannable): CharSequence {
        if (text.length > 0) {
            return text.substring(1, text.length)
        }
        // Should not happen
        return ""
    }

    override fun onDismiss(text: Spannable?) {
    }

    // Only if text which starts with '/' and without space
    override fun shouldShowPopup(text: Spannable?, cursorPos: Int): Boolean {
        return enabled && text?.startsWith("/") == true &&
                !text.contains(" ")
    }

    override fun shouldDismissPopup(text: Spannable?, cursorPos: Int): Boolean {
        return !shouldShowPopup(text, cursorPos)
    }
}
