package com.imzqqq.app.core.resources

import com.imzqqq.app.features.settings.VectorPreferences
import javax.inject.Inject

class UserPreferencesProvider @Inject constructor(private val vectorPreferences: VectorPreferences) {

    fun shouldShowHiddenEvents(): Boolean {
        return vectorPreferences.shouldShowHiddenEvents()
    }

    fun shouldShowReadReceipts(): Boolean {
        return vectorPreferences.showReadReceipts()
    }

    fun shouldShowRedactedMessages(): Boolean {
        return vectorPreferences.showRedactedMessages()
    }

    fun shouldShowLongClickOnRoomHelp(): Boolean {
        return vectorPreferences.shouldShowLongClickOnRoomHelp()
    }

    fun neverShowLongClickOnRoomHelpAgain() {
        vectorPreferences.neverShowLongClickOnRoomHelpAgain()
    }

    fun shouldShowJoinLeaves(): Boolean {
        return vectorPreferences.showJoinLeaveMessages()
    }

    fun shouldShowAvatarDisplayNameChanges(): Boolean {
        return vectorPreferences.showAvatarDisplayNameChangeMessages()
    }
}
