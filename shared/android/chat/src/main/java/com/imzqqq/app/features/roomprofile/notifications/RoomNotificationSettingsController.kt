package com.imzqqq.app.features.roomprofile.notifications

import androidx.annotation.StringRes
import com.airbnb.epoxy.TypedEpoxyController
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.profiles.notifications.notificationSettingsFooterItem
import com.imzqqq.app.core.epoxy.profiles.notifications.radioButtonItem
import com.imzqqq.app.core.epoxy.profiles.notifications.textHeaderItem
import org.matrix.android.sdk.api.session.room.notification.RoomNotificationState
import javax.inject.Inject

class RoomNotificationSettingsController @Inject constructor() : TypedEpoxyController<RoomNotificationSettingsViewState>() {

    interface Callback {
        fun didSelectRoomNotificationState(roomNotificationState: RoomNotificationState)
        fun didSelectAccountSettingsLink()
    }

    var callback: Callback? = null

    init {
        setData(null)
    }

    override fun buildModels(data: RoomNotificationSettingsViewState?) {
        val host = this
        data ?: return

        textHeaderItem {
            id("roomNotificationSettingsHeader")
            textRes(R.string.room_settings_room_notifications_notify_me)
        }
        data.notificationOptions.forEach {  notificationState ->
            val title = titleForNotificationState(notificationState)
            radioButtonItem {
                id(notificationState.name)
                titleRes(title)
                selected(data.notificationStateMapped() == notificationState)
                listener {
                    host.callback?.didSelectRoomNotificationState(notificationState)
                }
            }
        }
        notificationSettingsFooterItem {
            id("roomNotificationSettingsFooter")
            encrypted(data.roomSummary()?.isEncrypted == true)
            clickListener {
                host.callback?.didSelectAccountSettingsLink()
            }
        }
    }

    @StringRes
    private fun titleForNotificationState(notificationState: RoomNotificationState): Int? = when (notificationState) {
        RoomNotificationState.ALL_MESSAGES_NOISY -> R.string.room_settings_all_messages
        RoomNotificationState.MENTIONS_ONLY      -> R.string.room_settings_mention_and_keyword_only
        RoomNotificationState.MUTE               -> R.string.room_settings_none
        else -> null
    }
}
