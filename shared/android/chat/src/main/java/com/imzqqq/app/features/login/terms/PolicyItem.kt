package com.imzqqq.app.features.login.terms

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

@EpoxyModelClass(layout = R.layout.item_policy)
abstract class PolicyItem : EpoxyModelWithHolder<PolicyItem.Holder>() {
    @EpoxyAttribute
    var checked: Boolean = false

    @EpoxyAttribute
    var title: String? = null

    @EpoxyAttribute
    var subtitle: String? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var checkChangeListener: CompoundButton.OnCheckedChangeListener? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var clickListener: ClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.let {
            it.checkbox.isChecked = checked
            it.checkbox.setOnCheckedChangeListener(checkChangeListener)
            it.title.text = title
            it.subtitle.text = subtitle
            it.view.onClick(clickListener)
        }
    }

    // Ensure checkbox behaves as expected (remove the listener)
    override fun unbind(holder: Holder) {
        super.unbind(holder)
        holder.checkbox.setOnCheckedChangeListener(null)
    }

    class Holder : VectorEpoxyHolder() {
        val checkbox by bind<CheckBox>(R.id.adapter_item_policy_checkbox)
        val title by bind<TextView>(R.id.adapter_item_policy_title)
        val subtitle by bind<TextView>(R.id.adapter_item_policy_subtitle)
    }
}
