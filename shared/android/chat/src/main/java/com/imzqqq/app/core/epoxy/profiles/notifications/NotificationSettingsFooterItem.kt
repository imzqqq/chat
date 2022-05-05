package com.imzqqq.app.core.epoxy.profiles.notifications

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.extensions.setTextWithColoredPart

@EpoxyModelClass(layout = R.layout.item_notifications_footer)
abstract class NotificationSettingsFooterItem : VectorEpoxyModel<NotificationSettingsFooterItem.Holder>() {

    @EpoxyAttribute
    var encrypted: Boolean = false

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var clickListener: ClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        val accountSettingsString = holder.view.context.getString(R.string.room_settings_room_notifications_account_settings)
        val manageNotificationsString = holder.view.context.getString(
                R.string.room_settings_room_notifications_manage_notifications,
                accountSettingsString
        )
        val manageNotificationsBuilder = StringBuilder(manageNotificationsString)
        if (encrypted) {
            val encryptionNotice = holder.view.context.getString(R.string.room_settings_room_notifications_encryption_notice)
            manageNotificationsBuilder.appendLine().append(encryptionNotice)
        }

        holder.textView.setTextWithColoredPart(
                manageNotificationsBuilder.toString(),
                accountSettingsString,
                underline = true
        ) {
            clickListener?.invoke(holder.textView)
        }
    }

    class Holder : VectorEpoxyHolder() {
        val textView by bind<TextView>(R.id.footerText)
    }
}
