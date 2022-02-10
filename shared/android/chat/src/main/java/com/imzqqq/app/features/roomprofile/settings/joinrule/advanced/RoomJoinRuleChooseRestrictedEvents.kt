package com.imzqqq.app.features.roomprofile.settings.joinrule.advanced

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class RoomJoinRuleChooseRestrictedEvents : VectorViewEvents {
    object NavigateToChooseRestricted : RoomJoinRuleChooseRestrictedEvents()
    data class NavigateToUpgradeRoom(val roomId: String, val toVersion: String, val description: CharSequence) : RoomJoinRuleChooseRestrictedEvents()
}
