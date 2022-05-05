package com.imzqqq.app.features.discovery

import android.widget.Button
import androidx.annotation.StringRes
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.attributes.ButtonStyle
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.core.extensions.setTextOrHide
import com.imzqqq.app.core.resources.ColorProvider

@EpoxyModelClass(layout = R.layout.item_settings_button)
abstract class SettingsButtonItem : EpoxyModelWithHolder<SettingsButtonItem.Holder>() {

    @EpoxyAttribute
    lateinit var colorProvider: ColorProvider

    @EpoxyAttribute
    var buttonTitle: String? = null

    @EpoxyAttribute
    @StringRes
    var buttonTitleId: Int? = null

    @EpoxyAttribute
    var buttonStyle: ButtonStyle = ButtonStyle.POSITIVE

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var buttonClickListener: ClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        if (buttonTitleId != null) {
            holder.button.setText(buttonTitleId!!)
        } else {
            holder.button.setTextOrHide(buttonTitle)
        }

        when (buttonStyle) {
            ButtonStyle.POSITIVE    -> {
                holder.button.setTextColor(colorProvider.getColorFromAttribute(R.attr.colorPrimary))
            }
            ButtonStyle.DESTRUCTIVE -> {
                holder.button.setTextColor(colorProvider.getColorFromAttribute(R.attr.colorError))
            }
        }

        holder.button.onClick(buttonClickListener)
    }

    class Holder : VectorEpoxyHolder() {
        val button by bind<Button>(R.id.settings_item_button)
    }
}
