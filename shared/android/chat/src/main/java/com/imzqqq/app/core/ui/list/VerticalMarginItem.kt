package com.imzqqq.app.core.ui.list

import android.view.View
import androidx.core.view.updateLayoutParams
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel

/**
 * A generic item with empty space.
 */
@EpoxyModelClass(layout = R.layout.item_vertical_margin)
abstract class VerticalMarginItem : VectorEpoxyModel<VerticalMarginItem.Holder>() {

    @EpoxyAttribute
    var heightInPx: Int = 0

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.space.updateLayoutParams {
            height = heightInPx
        }
    }

    class Holder : VectorEpoxyHolder() {
        val space by bind<View>(R.id.item_vertical_margin_space)
    }
}
