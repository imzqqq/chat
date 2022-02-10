package com.imzqqq.app.features.roomprofile

import com.airbnb.epoxy.TypedEpoxyController
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.expandableTextItem
import com.imzqqq.app.core.epoxy.profiles.buildProfileAction
import com.imzqqq.app.core.epoxy.profiles.buildProfileSection
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.list.genericFooterItem
import com.imzqqq.app.core.ui.list.genericPositiveButtonItem
import com.imzqqq.app.features.home.ShortcutCreator
import com.imzqqq.app.features.home.room.detail.timeline.TimelineEventController
import com.imzqqq.app.features.home.room.detail.timeline.tools.createLinkMovementMethod
import com.imzqqq.app.features.settings.VectorPreferences
import org.matrix.android.sdk.api.crypto.RoomEncryptionTrustLevel
import org.matrix.android.sdk.api.session.room.model.RoomSummary
import javax.inject.Inject

class RoomProfileController @Inject constructor(
        private val stringProvider: StringProvider,
        private val colorProvider: ColorProvider,
        private val vectorPreferences: VectorPreferences,
        private val shortcutCreator: ShortcutCreator
) : TypedEpoxyController<RoomProfileViewState>() {

    var callback: Callback? = null

    interface Callback {
        fun onLearnMoreClicked()
        fun onEnableEncryptionClicked()
        fun onMemberListClicked()
        fun onBannedMemberListClicked()
        fun onNotificationsClicked()
        fun onUploadsClicked()
        fun createShortcut()
        fun onSettingsClicked()
        fun onLeaveRoomClicked()
        fun onRoomAliasesClicked()
        fun onRoomPermissionsClicked()
        fun onRoomIdClicked()
        fun onRoomDevToolsClicked()
        fun onUrlInTopicLongClicked(url: String)
        fun doMigrateToVersion(newVersion: String)
    }

    override fun buildModels(data: RoomProfileViewState?) {
        data ?: return
        val host = this
        val roomSummary = data.roomSummary() ?: return
        val enableNonSimplifiedMode = !vectorPreferences.simplifiedMode()

        // Topic
        roomSummary
                .topic
                .takeIf { it.isNotEmpty() }
                ?.let {
                    buildProfileSection(stringProvider.getString(R.string.room_settings_topic))
                    expandableTextItem {
                        id("topic")
                        content(it)
                        maxLines(5)
                        minCollapsedLines(3)
                        movementMethod(createLinkMovementMethod(object : TimelineEventController.UrlClickCallback {
                            override fun onUrlClicked(url: String, title: String): Boolean {
                                return false
                            }

                            override fun onUrlLongClicked(url: String): Boolean {
                                host.callback?.onUrlInTopicLongClicked(url)
                                return true
                            }
                        }))
                    }
                }

        // Security
        buildProfileSection(stringProvider.getString(R.string.room_profile_section_security))

        // Upgrade warning
        val roomVersion = data.roomCreateContent()?.roomVersion
        if (data.canUpgradeRoom &&
                !data.isTombstoned &&
                roomVersion != null &&
                data.isUsingUnstableRoomVersion &&
                data.recommendedRoomVersion != null) {
            genericFooterItem {
                id("version_warning")
                text(host.stringProvider.getString(R.string.room_using_unstable_room_version, roomVersion))
                textColor(host.colorProvider.getColorFromAttribute(R.attr.colorError))
                centered(false)
            }

            genericPositiveButtonItem {
                id("migrate_button")
                text(host.stringProvider.getString(R.string.room_upgrade_to_recommended_version))
                buttonClickAction { host.callback?.doMigrateToVersion(data.recommendedRoomVersion) }
            }
        }

        val learnMoreSubtitle = if (roomSummary.isEncrypted) {
            if (roomSummary.isDirect) R.string.direct_room_profile_encrypted_subtitle else R.string.room_profile_encrypted_subtitle
        } else {
            if (roomSummary.isDirect) R.string.direct_room_profile_not_encrypted_subtitle else R.string.room_profile_not_encrypted_subtitle
        }
        genericFooterItem {
            id("e2e info")
            centered(false)
            text(host.stringProvider.getString(learnMoreSubtitle))
            backgroundColor(host.colorProvider.getColorFromAttribute(R.attr.vctr_header_background))
        }
        // SC: Move down in the list, this one-time action is not important to enough to show this prevalent at the top
        //buildEncryptionAction(data.actionPermissions, roomSummary)

        // More
        buildProfileSection(stringProvider.getString(R.string.room_profile_section_more))
        buildProfileAction(
                id = "settings",
                title = stringProvider.getString(if (roomSummary.isDirect) {
                    R.string.direct_room_profile_section_more_settings
                } else {
                    R.string.room_profile_section_more_settings
                }),
                icon = R.drawable.ic_room_profile_settings,
                action = { callback?.onSettingsClicked() }
        )
        buildProfileAction(
                id = "notifications",
                title = stringProvider.getString(R.string.room_profile_section_more_notifications),
                icon = R.drawable.ic_room_profile_notification,
                action = { callback?.onNotificationsClicked() }
        )
        val numberOfMembers = roomSummary.joinedMembersCount ?: 0
        val hasWarning = roomSummary.isEncrypted && roomSummary.roomEncryptionTrustLevel == RoomEncryptionTrustLevel.Warning
        buildProfileAction(
                id = "member_list",
                title = stringProvider.getQuantityString(R.plurals.room_profile_section_more_member_list, numberOfMembers, numberOfMembers),
                icon = R.drawable.ic_room_profile_member_list,
                accessory = R.drawable.ic_shield_warning.takeIf { hasWarning } ?: 0,
                action = { callback?.onMemberListClicked() }
        )

        if (data.bannedMembership.invoke()?.isNotEmpty() == true) {
            buildProfileAction(
                    id = "banned_list",
                    title = stringProvider.getString(R.string.room_settings_banned_users_title),
                    icon = R.drawable.ic_settings_root_labs,
                    action = { callback?.onBannedMemberListClicked() }
            )
        }
        buildProfileAction(
                id = "uploads",
                title = stringProvider.getString(R.string.room_profile_section_more_uploads),
                icon = R.drawable.ic_room_profile_uploads,
                action = { callback?.onUploadsClicked() }
        )
        if (shortcutCreator.canCreateShortcut()) {
            buildProfileAction(
                    id = "shortcut",
                    title = stringProvider.getString(R.string.room_settings_add_homescreen_shortcut),
                    editable = false,
                    icon = R.drawable.ic_add_to_home_screen_24dp,
                    action = { callback?.createShortcut() }
            )
        }
        if (enableNonSimplifiedMode) {
            buildEncryptionAction(data.actionPermissions, roomSummary)
        }
        buildProfileAction(
                id = "leave",
                title = stringProvider.getString(if (roomSummary.isDirect) {
                    R.string.direct_room_profile_section_more_leave
                } else {
                    R.string.room_profile_section_more_leave
                }),
                divider = false,
                destructive = true,
                icon = R.drawable.ic_room_actions_leave,
                editable = false,
                action = { callback?.onLeaveRoomClicked() }
        )

        // Advanced
        if (enableNonSimplifiedMode || vectorPreferences.developerMode()) {
            buildProfileSection(stringProvider.getString(R.string.room_settings_category_advanced_title))
        }

        if (enableNonSimplifiedMode) {
            buildProfileAction(
                    id = "alias",
                    title = stringProvider.getString(R.string.room_settings_alias_title),
                    subtitle = stringProvider.getString(R.string.room_settings_alias_subtitle),
                    divider = true,
                    editable = true,
                    action = { callback?.onRoomAliasesClicked() }
            )

            buildProfileAction(
                    id = "permissions",
                    title = stringProvider.getString(R.string.room_settings_permissions_title),
                    subtitle = stringProvider.getString(R.string.room_settings_permissions_subtitle),
                    divider = vectorPreferences.developerMode(),
                    editable = true,
                    action = { callback?.onRoomPermissionsClicked() }
            )
        }

        if (vectorPreferences.developerMode()) {
            buildProfileAction(
                    id = "roomId",
                    title = stringProvider.getString(R.string.room_settings_room_internal_id),
                    subtitle = roomSummary.roomId,
                    divider = true,
                    editable = false,
                    action = { callback?.onRoomIdClicked() }
            )
            roomVersion?.let {
                buildProfileAction(
                        id = "roomVersion",
                        title = stringProvider.getString(R.string.room_settings_room_version_title),
                        subtitle = it,
                        divider = true,
                        editable = false
                )
            }
            buildProfileAction(
                    id = "devTools",
                    title = stringProvider.getString(R.string.dev_tools_menu_name),
                    divider = false,
                    editable = true,
                    action = { callback?.onRoomDevToolsClicked() }
            )
        }
    }

    private fun buildEncryptionAction(actionPermissions: RoomProfileViewState.ActionPermissions, roomSummary: RoomSummary) {
        if (!roomSummary.isEncrypted) {
            if (actionPermissions.canEnableEncryption) {
                buildProfileAction(
                        id = "enableEncryption",
                        title = stringProvider.getString(R.string.room_settings_enable_encryption),
                        icon = R.drawable.ic_shield_black,
                        divider = true,
                        editable = false,
                        action = { callback?.onEnableEncryptionClicked() }
                )
            /* SC: disable useless button
            } else {
                buildProfileAction(
                        id = "enableEncryption",
                        title = stringProvider.getString(R.string.room_settings_enable_encryption_no_permission),
                        icon = R.drawable.ic_shield_black,
                        divider = false,
                        editable = false
                )
            */
            }
        }
    }
}
