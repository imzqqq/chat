package com.imzqqq.app.core.ui.list

import android.content.res.ColorStateList
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.core.extensions.setTextOrHide

/**
 * A generic list item to display when there is no results, with an optional CTA
 */
@EpoxyModelClass(layout = R.layout.item_generic_empty_state)
abstract class GenericEmptyWithActionItem : VectorEpoxyModel<GenericEmptyWithActionItem.Holder>() {

    @EpoxyAttribute
    var title: CharSequence? = null

    @EpoxyAttribute
    var description: CharSequence? = null

    @EpoxyAttribute
    @DrawableRes
    var iconRes: Int = -1

    @EpoxyAttribute
    @ColorInt
    var iconTint: Int? = null

    @EpoxyAttribute
    var buttonAction: Action? = null

    override fun bind(holder: Holder) {
        super.bind(holder)

        holder.titleText.setTextOrHide(title)
        holder.descriptionText.setTextOrHide(description)

        if (iconRes != -1) {
            holder.imageView.setImageResource(iconRes)
            holder.imageView.isVisible = true
            if (iconTint != null) {
                ImageViewCompat.setImageTintList(holder.imageView, ColorStateList.valueOf(iconTint!!))
            } else {
                ImageViewCompat.setImageTintList(holder.imageView, null)
            }
        } else {
            holder.imageView.isVisible = false
        }

        holder.actionButton.setTextOrHide(buttonAction?.title)
        holder.actionButton.onClick(buttonAction?.listener)
    }

    class Holder : VectorEpoxyHolder() {
        val root by bind<View>(R.id.item_generic_root)
        val titleText by bind<TextView>(R.id.emptyItemTitleView)
        val descriptionText by bind<TextView>(R.id.emptyItemMessageView)
        val imageView by bind<ImageView>(R.id.emptyItemImageView)
        val actionButton by bind<Button>(R.id.emptyItemButton)
    }
}
