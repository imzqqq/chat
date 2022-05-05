package com.imzqqq.app.features.discovery

import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.core.extensions.setTextOrHide

@EpoxyModelClass(layout = R.layout.item_settings_helper_info)
abstract class SettingsInfoItem : EpoxyModelWithHolder<SettingsInfoItem.Holder>() {

    @EpoxyAttribute
    var helperText: String? = null

    @EpoxyAttribute
    @StringRes
    var helperTextResId: Int? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var itemClickListener: ClickListener? = null

    @EpoxyAttribute
    @DrawableRes
    var compoundDrawable: Int = R.drawable.vector_warning_red

    @EpoxyAttribute
    var showCompoundDrawable: Boolean = false

    override fun bind(holder: Holder) {
        super.bind(holder)

        if (helperTextResId != null) {
            holder.text.setText(helperTextResId!!)
        } else {
            holder.text.setTextOrHide(helperText)
        }

        holder.view.onClick(itemClickListener)

        if (showCompoundDrawable) {
            holder.text.setCompoundDrawablesWithIntrinsicBounds(compoundDrawable, 0, 0, 0)
        } else {
            holder.text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
    }

    class Holder : VectorEpoxyHolder() {
        val text by bind<TextView>(R.id.settings_helper_text)
    }
}
