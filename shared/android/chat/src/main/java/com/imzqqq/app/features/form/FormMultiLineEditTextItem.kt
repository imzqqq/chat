package com.imzqqq.app.features.form

import android.graphics.Typeface
import android.text.Editable
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.TextListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.addTextChangedListenerOnce
import com.imzqqq.app.core.epoxy.setValueOnce
import com.imzqqq.app.core.platform.SimpleTextWatcher

@EpoxyModelClass(layout = R.layout.item_form_multiline_text_input)
abstract class FormMultiLineEditTextItem : VectorEpoxyModel<FormMultiLineEditTextItem.Holder>() {

    @EpoxyAttribute
    var hint: String? = null

    @EpoxyAttribute
    var value: String? = null

    @EpoxyAttribute
    var errorMessage: String? = null

    @EpoxyAttribute
    var enabled: Boolean = true

    @EpoxyAttribute
    var textSizeSp: Int? = null

    @EpoxyAttribute
    var minLines: Int = 3

    @EpoxyAttribute
    var typeFace: Typeface = Typeface.DEFAULT

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onTextChange: TextListener? = null

    private val onTextChangeListener = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable) {
            onTextChange?.invoke(s.toString())
        }
    }

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.textInputLayout.isEnabled = enabled
        holder.textInputLayout.hint = hint
        holder.textInputLayout.error = errorMessage

        holder.textInputEditText.typeface = typeFace
        holder.textInputEditText.textSize = textSizeSp?.toFloat() ?: 14f
        holder.textInputEditText.minLines = minLines

        holder.setValueOnce(holder.textInputEditText, value)

        holder.textInputEditText.isEnabled = enabled

        holder.textInputEditText.addTextChangedListenerOnce(onTextChangeListener)
    }

    override fun shouldSaveViewState(): Boolean {
        return false
    }

    override fun unbind(holder: Holder) {
        super.unbind(holder)
        holder.textInputEditText.removeTextChangedListener(onTextChangeListener)
    }

    class Holder : VectorEpoxyHolder() {
        val textInputLayout by bind<TextInputLayout>(R.id.formMultiLineTextInputLayout)
        val textInputEditText by bind<TextInputEditText>(R.id.formMultiLineEditText)
    }
}
