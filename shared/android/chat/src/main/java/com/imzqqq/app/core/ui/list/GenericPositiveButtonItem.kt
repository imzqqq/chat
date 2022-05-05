package com.imzqqq.app.core.ui.list

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

/**
 * A generic button list item.
 */
@EpoxyModelClass(layout = R.layout.item_positive_button)
abstract class GenericPositiveButtonItem : VectorEpoxyModel<GenericPositiveButtonItem.Holder>() {

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

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.button.text = text
        if (iconRes != null) {
            holder.button.setIconResource(iconRes!!)
        } else {
            holder.button.icon = null
        }
        holder.button.onClick(buttonClickAction)
    }

    class Holder : VectorEpoxyHolder() {
        val button by bind<MaterialButton>(R.id.itemGenericItemButton)
    }
}
