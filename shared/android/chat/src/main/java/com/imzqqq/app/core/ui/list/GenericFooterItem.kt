package com.imzqqq.app.core.ui.list

import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.core.extensions.setTextOrHide
import com.imzqqq.app.features.themes.ThemeUtils

/**
 * A generic list item.
 * Displays an item with a title, and optional description.
 * Can display an accessory on the right, that can be an image or an indeterminate progress.
 * If provided with an action, will display a button at the bottom of the list item.
 */
@EpoxyModelClass(layout = R.layout.item_generic_footer)
abstract class GenericFooterItem : VectorEpoxyModel<GenericFooterItem.Holder>() {

    @EpoxyAttribute
    var text: CharSequence? = null

    @EpoxyAttribute
    var style: ItemStyle = ItemStyle.NORMAL_TEXT

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var itemClickAction: ClickListener? = null

    @EpoxyAttribute
    var centered: Boolean = true

    @EpoxyAttribute
    @ColorInt
    var textColor: Int? = null

    @EpoxyAttribute
    @ColorInt
    var backgroundColor: Int? = null

    override fun bind(holder: Holder) {
        super.bind(holder)

        holder.text.setTextOrHide(text)
        holder.text.typeface = style.toTypeFace()
        holder.text.textSize = style.toTextSize()
        holder.text.gravity = if (centered) Gravity.CENTER_HORIZONTAL else Gravity.START

        if (textColor != null) {
            holder.text.setTextColor(textColor!!)
        } else {
            holder.text.setTextColor(ThemeUtils.getColor(holder.view.context, R.attr.vctr_content_secondary))
        }

        if (backgroundColor != null) {
            holder.layout.setBackgroundColor(backgroundColor!!)
        } else  {
            holder.layout.background = null
        }

        holder.view.onClick(itemClickAction)
    }

    class Holder : VectorEpoxyHolder() {
        val text by bind<TextView>(R.id.itemGenericFooterText)
        val layout by bind<LinearLayout>(R.id.itemGenericFooterLayout)
    }
}
