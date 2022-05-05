package com.imzqqq.app.features.home.room.list.actions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.airbnb.epoxy.TypedEpoxyController
import com.imzqqq.app.BuildConfig
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.bottomSheetDividerItem
import com.imzqqq.app.core.epoxy.bottomsheet.bottomSheetActionItem
import com.imzqqq.app.core.epoxy.bottomsheet.bottomSheetRoomPreviewItem
import com.imzqqq.app.core.epoxy.profiles.notifications.bottomSheetRadioButtonItem
import com.imzqqq.app.core.epoxy.profiles.notifications.radioButtonItem
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.features.home.AvatarRenderer
import com.imzqqq.app.features.roomprofile.notifications.notificationOptions
import com.imzqqq.app.features.roomprofile.notifications.notificationStateMapped
import com.imzqqq.app.features.settings.VectorPreferences
import org.matrix.android.sdk.api.session.room.notification.RoomNotificationState
import org.matrix.android.sdk.api.util.toMatrixItem
import javax.inject.Inject

/**
 * Epoxy controller for room list actions
 */
class RoomListQuickActionsEpoxyController @Inject constructor(
        private val avatarRenderer: AvatarRenderer,
        private val colorProvider: ColorProvider,
        private val stringProvider: StringProvider,
        private val vectorPreferences: VectorPreferences,
) : TypedEpoxyController<RoomListQuickActionViewState>() {

    var listener: Listener? = null

    override fun buildModels(state: RoomListQuickActionViewState) {
        val notificationViewState = state.notificationSettingsViewState
        val roomSummary = notificationViewState.roomSummary() ?: return
        val host = this
        val isV2 = BuildConfig.USE_NOTIFICATION_SETTINGS_V2
        // V2 always shows full details as we no longer display the sheet from RoomProfile > Notifications
        val showFull = state.roomListActionsArgs.mode == RoomListActionsArgs.Mode.FULL || isV2

        if (showFull) {
            // Preview, favorite, settings
            bottomSheetRoomPreviewItem {
                id("room_preview")
                avatarRenderer(host.avatarRenderer)
                matrixItem(roomSummary.toMatrixItem())
                stringProvider(host.stringProvider)
                colorProvider(host.colorProvider)
                izLowPriority(roomSummary.isLowPriority)
                izFavorite(roomSummary.isFavorite)
                settingsClickListener { host.listener?.didSelectMenuAction(RoomListQuickActionsSharedAction.Settings(roomSummary.roomId)) }
                favoriteClickListener { host.listener?.didSelectMenuAction(RoomListQuickActionsSharedAction.Favorite(roomSummary.roomId)) }
                lowPriorityClickListener { host.listener?.didSelectMenuAction(RoomListQuickActionsSharedAction.LowPriority(roomSummary.roomId)) }
            }

            if (vectorPreferences.labAllowMarkUnread()) {
                // Mark read/unread
                bottomSheetDividerItem {
                    id("mark_unread_separator")
                }
                if (roomSummary.scIsUnread()) {
                    RoomListQuickActionsSharedAction.MarkRead(roomSummary.roomId).toBottomSheetItem(-2)
                } else {
                    RoomListQuickActionsSharedAction.MarkUnread(roomSummary.roomId).toBottomSheetItem(-2)
                }
            }

            if (vectorPreferences.loadRoomAtFirstUnread()) {
                // TODO can we check if position of roomSummary.readMarkerId is below or equal to
                // roomSummary.latestPreviewableOriginalContentEvent, and hide this otherwise?
                RoomListQuickActionsSharedAction.OpenAtBottom(roomSummary.roomId).toBottomSheetItem(-1)
            }

            // Notifications
            bottomSheetDividerItem {
                id("notifications_separator")
            }
        }

        if (isV2) {
            notificationViewState.notificationOptions.forEach {  notificationState ->
                val title = titleForNotificationState(notificationState)
                val icon = iconForNotificationState(notificationState)
                bottomSheetRadioButtonItem {
                    id(notificationState.name)
                    titleRes(title)
                    iconRes(icon)
                    selected(notificationViewState.notificationStateMapped() == notificationState)
                    listener {
                    host.listener?.didSelectRoomNotificationState(notificationState)
                    }
                }
            }
        } else {
            val selectedRoomState = notificationViewState.notificationState()
            RoomListQuickActionsSharedAction.NotificationsAllNoisy(roomSummary.roomId).toBottomSheetItem(0, selectedRoomState)
            RoomListQuickActionsSharedAction.NotificationsAll(roomSummary.roomId).toBottomSheetItem(1, selectedRoomState)
            RoomListQuickActionsSharedAction.NotificationsMentionsOnly(roomSummary.roomId).toBottomSheetItem(2, selectedRoomState)
            RoomListQuickActionsSharedAction.NotificationsMute(roomSummary.roomId).toBottomSheetItem(3, selectedRoomState)
        }

        if (showFull) {
            RoomListQuickActionsSharedAction.Leave(roomSummary.roomId, showIcon = true /*!isV2*/).toBottomSheetItem(5)
        }
    }

    @StringRes
    private fun titleForNotificationState(notificationState: RoomNotificationState): Int? = when (notificationState) {
        RoomNotificationState.ALL_MESSAGES_NOISY -> R.string.room_settings_all_messages
        RoomNotificationState.MENTIONS_ONLY      -> R.string.room_settings_mention_and_keyword_only
        RoomNotificationState.MUTE               -> R.string.room_settings_none
        else -> null
    }

    @DrawableRes
    private fun iconForNotificationState(notificationState: RoomNotificationState): Int? = when (notificationState) {
        RoomNotificationState.ALL_MESSAGES_NOISY -> R.drawable.ic_room_actions_notifications_all_noisy
        RoomNotificationState.ALL_MESSAGES       -> R.drawable.ic_room_actions_notifications_all
        RoomNotificationState.MENTIONS_ONLY      -> R.drawable.ic_room_actions_notifications_mentions
        RoomNotificationState.MUTE               -> R.drawable.ic_room_actions_notifications_mutes
        else -> null
    }

    private fun RoomListQuickActionsSharedAction.toBottomSheetItem(index: Int, roomNotificationState: RoomNotificationState? = null) {
        val host = this@RoomListQuickActionsEpoxyController
        val selected = when (this) {
            is RoomListQuickActionsSharedAction.NotificationsAllNoisy     -> roomNotificationState == RoomNotificationState.ALL_MESSAGES_NOISY
            is RoomListQuickActionsSharedAction.NotificationsAll          -> roomNotificationState == RoomNotificationState.ALL_MESSAGES
            is RoomListQuickActionsSharedAction.NotificationsMentionsOnly -> roomNotificationState == RoomNotificationState.MENTIONS_ONLY
            is RoomListQuickActionsSharedAction.NotificationsMute         -> roomNotificationState == RoomNotificationState.MUTE
            else                                                          -> false
        }
        return bottomSheetActionItem {
            id("action_$index")
            selected(selected)
            if (iconResId != null) {
                iconRes(iconResId)
            } else {
                showIcon(false)
            }
            textRes(titleRes)
            destructive(this@toBottomSheetItem.destructive)
            listener { host.listener?.didSelectMenuAction(this@toBottomSheetItem) }
        }
    }

    interface Listener {
        fun didSelectMenuAction(quickAction: RoomListQuickActionsSharedAction)
        fun didSelectRoomNotificationState(roomNotificationState: RoomNotificationState)
    }
}
