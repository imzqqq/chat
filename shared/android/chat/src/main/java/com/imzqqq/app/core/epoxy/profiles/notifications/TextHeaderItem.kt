package com.imzqqq.app.core.epoxy.profiles.notifications

import android.widget.TextView
import androidx.annotation.StringRes
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel

@EpoxyModelClass(layout = R.layout.item_text_header)
abstract class TextHeaderItem : VectorEpoxyModel<TextHeaderItem.Holder>() {

    @EpoxyAttribute
    var text: String? = null

    @StringRes
    @EpoxyAttribute
    var textRes: Int? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        val textResource = textRes
        if (textResource != null) {
            holder.textView.setText(textResource)
        } else {
            holder.textView.text = text
        }
    }

    class Holder : VectorEpoxyHolder() {
        val textView by bind<TextView>(R.id.headerText)
    }
}
