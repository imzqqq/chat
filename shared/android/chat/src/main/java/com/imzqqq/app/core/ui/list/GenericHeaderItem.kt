package com.imzqqq.app.core.ui.list

import android.widget.TextView
import androidx.annotation.ColorInt
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.extensions.setTextOrHide
import com.imzqqq.app.features.themes.ThemeUtils

/**
 * A generic list item header left aligned with notice color.
 */
@EpoxyModelClass(layout = R.layout.item_generic_header)
abstract class GenericHeaderItem : VectorEpoxyModel<GenericHeaderItem.Holder>() {

    @EpoxyAttribute
    var text: String? = null

    @EpoxyAttribute
    @ColorInt
    var textColor: Int? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.text.setTextOrHide(text)
        if (textColor != null) {
            holder.text.setTextColor(textColor!!)
        } else {
            holder.text.setTextColor(ThemeUtils.getColor(holder.view.context, R.attr.vctr_notice_text_color))
        }
    }

    class Holder : VectorEpoxyHolder() {
        val text by bind<TextView>(R.id.itemGenericHeaderText)
    }
}
