package com.imzqqq.app.features.terms

import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.onClick

@EpoxyModelClass(layout = R.layout.item_tos)
abstract class TermItem : EpoxyModelWithHolder<TermItem.Holder>() {

    @EpoxyAttribute
    var checked: Boolean = false

    @EpoxyAttribute
    var name: String? = null

    @EpoxyAttribute
    var description: String? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var checkChangeListener: CompoundButton.OnCheckedChangeListener? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var clickListener: ClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.checkbox.isChecked = checked
        holder.title.text = name
        holder.description.text = description
        holder.checkbox.setOnCheckedChangeListener(checkChangeListener)
        holder.view.onClick(clickListener)
    }

    class Holder : VectorEpoxyHolder() {
        val checkbox by bind<CheckBox>(R.id.term_accept_checkbox)
        val title by bind<TextView>(R.id.term_name)
        val description by bind<TextView>(R.id.term_description)
    }
}
