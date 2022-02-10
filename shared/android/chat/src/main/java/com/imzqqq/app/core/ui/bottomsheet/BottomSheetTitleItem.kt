package com.imzqqq.app.core.ui.bottomsheet

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.extensions.setTextOrHide

/**
 * A title for bottom sheet, with an optional subtitle. It does not include the bottom separator.
 */
@EpoxyModelClass(layout = R.layout.item_bottom_sheet_title)
abstract class BottomSheetTitleItem : VectorEpoxyModel<BottomSheetTitleItem.Holder>() {

    @EpoxyAttribute
    lateinit var title: String

    @EpoxyAttribute
    var subTitle: String? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.title.text = title
        holder.subtitle.setTextOrHide(subTitle)
    }

    class Holder : VectorEpoxyHolder() {
        val title by bind<TextView>(R.id.itemBottomSheetTitleTitle)
        val subtitle by bind<TextView>(R.id.itemBottomSheetTitleSubtitle)
    }
}
