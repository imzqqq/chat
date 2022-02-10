package com.imzqqq.app.features.form

import android.text.Editable
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.TextListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.addTextChangedListenerOnce
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.core.extensions.setTextIfDifferent
import com.imzqqq.app.core.platform.SimpleTextWatcher

@EpoxyModelClass(layout = R.layout.item_form_text_input_with_delete)
abstract class FormEditTextWithDeleteItem : VectorEpoxyModel<FormEditTextWithDeleteItem.Holder>() {

    @EpoxyAttribute
    var hint: String? = null

    @EpoxyAttribute
    var value: String? = null

    @EpoxyAttribute
    var enabled: Boolean = true

    @EpoxyAttribute
    var singleLine: Boolean = true

    @EpoxyAttribute
    var imeOptions: Int? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onTextChange: TextListener? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onDeleteClicked: ClickListener? = null

    private val onTextChangeListener = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable) {
            onTextChange?.invoke(s.toString())
        }
    }

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.textInputLayout.isEnabled = enabled
        holder.textInputLayout.hint = hint

        holder.textInputEditText.setTextIfDifferent(value)

        holder.textInputEditText.isEnabled = enabled
        holder.textInputEditText.isSingleLine = singleLine

        holder.textInputEditText.imeOptions =
                imeOptions ?: when (singleLine) {
                    true  -> EditorInfo.IME_ACTION_NEXT
                    false -> EditorInfo.IME_ACTION_NONE
                }

        holder.textInputEditText.addTextChangedListenerOnce(onTextChangeListener)

        holder.textInputDeleteButton.onClick(onDeleteClicked)
    }

    override fun shouldSaveViewState(): Boolean {
        return false
    }

    override fun unbind(holder: Holder) {
        super.unbind(holder)
        holder.textInputEditText.removeTextChangedListener(onTextChangeListener)
    }

    class Holder : VectorEpoxyHolder() {
        val textInputLayout by bind<TextInputLayout>(R.id.formTextInputTextInputLayout)
        val textInputEditText by bind<TextInputEditText>(R.id.formTextInputTextInputEditText)
        val textInputDeleteButton by bind<ImageButton>(R.id.formTextInputDeleteButton)
    }
}
