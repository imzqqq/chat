package com.imzqqq.app.features.discovery

import android.widget.TextView
import androidx.annotation.StringRes
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.extensions.setTextOrHide

@EpoxyModelClass(layout = R.layout.item_settings_section_title)
abstract class SettingsSectionTitleItem : EpoxyModelWithHolder<SettingsSectionTitleItem.Holder>() {

    @EpoxyAttribute
    var title: String? = null

    @EpoxyAttribute
    @StringRes
    var titleResId: Int? = null

    override fun bind(holder: Holder) {
        super.bind(holder)

        if (titleResId != null) {
            holder.textView.setText(titleResId!!)
        } else {
            holder.textView.setTextOrHide(title)
        }
    }

    class Holder : VectorEpoxyHolder() {
        val textView by bind<TextView>(R.id.settings_section_title_text)
    }
}
