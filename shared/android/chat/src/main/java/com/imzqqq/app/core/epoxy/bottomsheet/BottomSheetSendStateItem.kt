package com.imzqqq.app.core.epoxy.bottomsheet

import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel

/**
 * A send state for bottom sheet.
 */
@EpoxyModelClass(layout = R.layout.item_bottom_sheet_message_status)
abstract class BottomSheetSendStateItem : VectorEpoxyModel<BottomSheetSendStateItem.Holder>() {

    @EpoxyAttribute
    var showProgress: Boolean = false

    @EpoxyAttribute
    lateinit var text: CharSequence

    @EpoxyAttribute
    @DrawableRes
    var drawableStart: Int = 0

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.progress.isVisible = showProgress
        holder.text.setCompoundDrawablesWithIntrinsicBounds(drawableStart, 0, 0, 0)
        holder.text.text = text
    }

    class Holder : VectorEpoxyHolder() {
        val progress by bind<View>(R.id.messageStatusProgress)
        val text by bind<TextView>(R.id.messageStatusText)
    }
}
