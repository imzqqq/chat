package com.imzqqq.app.core.ui.list

import android.graphics.Typeface
import android.view.Gravity
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.google.android.material.button.MaterialButton
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.features.themes.ThemeUtils

/**
 * A generic button list item.
 */
@EpoxyModelClass(layout = R.layout.item_generic_button)
abstract class GenericButtonItem : VectorEpoxyModel<GenericButtonItem.Holder>() {

    @EpoxyAttribute
    var text: String? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var buttonClickAction: ClickListener? = null

    @EpoxyAttribute
    @ColorInt
    var textColor: Int? = null

    @EpoxyAttribute
    @DrawableRes
    var iconRes: Int? = null

    @EpoxyAttribute
    var gravity: Int = Gravity.CENTER

    @EpoxyAttribute
    var bold: Boolean = false

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.button.text = text
        val textColor = textColor ?: ThemeUtils.getColor(holder.view.context, R.attr.vctr_content_primary)
        holder.button.setTextColor(textColor)
        if (iconRes != null) {
            holder.button.setIconResource(iconRes!!)
        } else {
            holder.button.icon = null
        }

        holder.button.gravity = gravity or Gravity.CENTER_VERTICAL
        val textStyle = if (bold) Typeface.BOLD else Typeface.NORMAL
        holder.button.setTypeface(null, textStyle)

        holder.button.onClick(buttonClickAction)
    }

    class Holder : VectorEpoxyHolder() {
        val button by bind<MaterialButton>(R.id.itemGenericItemButton)
    }
}
