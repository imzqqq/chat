package com.imzqqq.app.features.discovery

import android.widget.Button
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.onClick

@EpoxyModelClass(layout = R.layout.item_settings_continue_cancel)
abstract class SettingsContinueCancelItem : EpoxyModelWithHolder<SettingsContinueCancelItem.Holder>() {

    @EpoxyAttribute
    var continueText: String? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var continueOnClick: ClickListener? = null

    @EpoxyAttribute
    var canContinue: Boolean = true

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var cancelOnClick: ClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)

        holder.cancelButton.onClick(cancelOnClick)

        continueText?.let { holder.continueButton.text = it }
        holder.continueButton.onClick(continueOnClick)
        holder.continueButton.isEnabled = canContinue
    }

    class Holder : VectorEpoxyHolder() {
        val cancelButton by bind<Button>(R.id.settings_item_cancel_button)
        val continueButton by bind<Button>(R.id.settings_item_continue_button)
    }
}
