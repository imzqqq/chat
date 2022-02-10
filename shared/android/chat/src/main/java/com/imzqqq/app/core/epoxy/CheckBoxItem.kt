package com.imzqqq.app.core.epoxy

import android.widget.CompoundButton
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.google.android.material.checkbox.MaterialCheckBox
import com.imzqqq.app.R

@EpoxyModelClass(layout = R.layout.item_checkbox)
abstract class CheckBoxItem : VectorEpoxyModel<CheckBoxItem.Holder>() {

    @EpoxyAttribute
    var checked: Boolean = false

    @EpoxyAttribute lateinit var title: String

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var checkChangeListener: CompoundButton.OnCheckedChangeListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.checkbox.isChecked = checked
        holder.checkbox.text = title
        holder.checkbox.setOnCheckedChangeListener(checkChangeListener)
    }

    class Holder : VectorEpoxyHolder() {
        val checkbox by bind<MaterialCheckBox>(R.id.checkbox)
    }
}
