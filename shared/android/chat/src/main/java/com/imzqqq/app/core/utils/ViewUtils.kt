package com.imzqqq.app.core.utils

import android.text.Editable
import android.view.ViewGroup
import androidx.core.view.children
import com.google.android.material.textfield.TextInputLayout
import com.imzqqq.app.core.platform.SimpleTextWatcher

/**
 * Find all TextInputLayout in a ViewGroup and in all its descendants
 */
fun ViewGroup.findAllTextInputLayout(): List<TextInputLayout> {
    val res = ArrayList<TextInputLayout>()

    children.forEach {
        if (it is TextInputLayout) {
            res.add(it)
        } else if (it is ViewGroup) {
            // Recursive call
            res.addAll(it.findAllTextInputLayout())
        }
    }

    return res
}

/**
 * Add a text change listener to all TextInputEditText to reset error on its TextInputLayout when the text is changed
 */
fun autoResetTextInputLayoutErrors(textInputLayouts: List<TextInputLayout>) {
    textInputLayouts.forEach {
        it.editText?.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                // Reset the error
                it.error = null
            }
        })
    }
}
