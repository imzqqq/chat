package com.imzqqq.app.features.form

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.features.themes.ThemeUtils

@EpoxyModelClass(layout = R.layout.item_form_advanced_toggle)
abstract class FormAdvancedToggleItem : VectorEpoxyModel<FormAdvancedToggleItem.Holder>() {

    @EpoxyAttribute lateinit var title: CharSequence
    @EpoxyAttribute var expanded: Boolean = false
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var listener: ClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        val tintColor = ThemeUtils.getColor(holder.view.context, R.attr.vctr_content_secondary)
        val expandedArrowDrawableRes = if (expanded) R.drawable.ic_expand_more else R.drawable.ic_expand_less
        val expandedArrowDrawable = ContextCompat.getDrawable(holder.view.context, expandedArrowDrawableRes)?.also {
            DrawableCompat.setTint(it, tintColor)
        }
        holder.titleView.setCompoundDrawablesWithIntrinsicBounds(null, null, expandedArrowDrawable, null)
        holder.titleView.text = title
        holder.view.onClick(listener)
    }

    class Holder : VectorEpoxyHolder() {
        val titleView by bind<TextView>(R.id.itemFormAdvancedToggleTitleView)
    }
}
