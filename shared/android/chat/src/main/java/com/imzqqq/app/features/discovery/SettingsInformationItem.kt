package com.imzqqq.app.features.discovery

import android.widget.TextView
import androidx.annotation.ColorInt
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_settings_information)
abstract class SettingsInformationItem : EpoxyModelWithHolder<SettingsInformationItem.Holder>() {

    @EpoxyAttribute
    lateinit var message: String

    @EpoxyAttribute
    @ColorInt
    var textColor: Int = 0

    override fun bind(holder: Holder) {
        super.bind(holder)

        holder.textView.text = message
        holder.textView.setTextColor(textColor)
    }

    class Holder : VectorEpoxyHolder() {
        val textView by bind<TextView>(R.id.settings_item_information)
    }
}
