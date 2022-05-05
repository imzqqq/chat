package com.imzqqq.app.features.crypto.keysbackup.settings

import android.widget.Button
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.core.extensions.setTextOrHide

@EpoxyModelClass(layout = R.layout.item_keys_backup_settings_button_footer)
abstract class KeysBackupSettingFooterItem : VectorEpoxyModel<KeysBackupSettingFooterItem.Holder>() {

    @EpoxyAttribute
    var textButton1: String? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var clickOnButton1: ClickListener? = null

    @EpoxyAttribute
    var textButton2: String? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var clickOnButton2: ClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.button1.setTextOrHide(textButton1)
        holder.button1.onClick(clickOnButton1)

        holder.button2.setTextOrHide(textButton2)
        holder.button2.onClick(clickOnButton2)
    }

    class Holder : VectorEpoxyHolder() {
        val button1 by bind<Button>(R.id.keys_backup_settings_footer_button1)
        val button2 by bind<TextView>(R.id.keys_backup_settings_footer_button2)
    }
}
