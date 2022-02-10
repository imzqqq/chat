package com.imzqqq.app.features.discovery

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_settings_centered_image)
abstract class SettingsCenteredImageItem : EpoxyModelWithHolder<SettingsCenteredImageItem.Holder>() {

    @EpoxyAttribute
    @DrawableRes
    var drawableRes: Int = 0

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.image.setImageResource(drawableRes)
    }

    class Holder : VectorEpoxyHolder() {
        val image by bind<ImageView>(R.id.itemSettingsImage)
    }
}
