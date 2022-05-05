@file:Suppress("DEPRECATION")

package com.imzqqq.app.flow.components.compose.view

import android.content.Context
import android.text.InputType
import android.text.method.KeyListener
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView
import androidx.core.view.inputmethod.EditorInfoCompat
import androidx.core.view.inputmethod.InputConnectionCompat
import androidx.emoji.widget.EmojiEditTextHelper

class EditTextTyped @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatMultiAutoCompleteTextView(context, attributeSet) {

    private var onCommitContentListener: InputConnectionCompat.OnCommitContentListener? = null
    private val emojiEditTextHelper: EmojiEditTextHelper = EmojiEditTextHelper(this)

    init {
        // fix a bug with autocomplete and some keyboards
        val newInputType = inputType and (inputType xor InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE)
        inputType = newInputType
        super.setKeyListener(getEmojiEditTextHelper().getKeyListener(keyListener))
    }

    /// MARK - imzqqq
    override fun setKeyListener(input: KeyListener?) {
        //super.setKeyListener(getEmojiEditTextHelper().getKeyListener(input))
        super.setKeyListener(input?.let { getEmojiEditTextHelper().getKeyListener(it) })
    }
    ///

    fun setOnCommitContentListener(listener: InputConnectionCompat.OnCommitContentListener) {
        onCommitContentListener = listener
    }

    override fun onCreateInputConnection(editorInfo: EditorInfo): InputConnection {
        val connection = super.onCreateInputConnection(editorInfo)
        return if (onCommitContentListener != null) {
            EditorInfoCompat.setContentMimeTypes(editorInfo, arrayOf("image/*"))
            getEmojiEditTextHelper().onCreateInputConnection(
                InputConnectionCompat.createWrapper(
                    connection, editorInfo,
                    onCommitContentListener!!
                ),
                editorInfo
            )!!
        } else {
            connection
        }
    }

    private fun getEmojiEditTextHelper(): EmojiEditTextHelper {
        return emojiEditTextHelper
    }
}
