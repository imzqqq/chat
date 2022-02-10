package com.imzqqq.app.features.roomprofile.settings.joinrule.advanced

import com.imzqqq.app.core.platform.VectorViewModelAction
import org.matrix.android.sdk.api.session.room.model.RoomJoinRules
import org.matrix.android.sdk.api.util.MatrixItem

sealed class RoomJoinRuleChooseRestrictedActions : VectorViewModelAction {
    data class FilterWith(val filter: String) : RoomJoinRuleChooseRestrictedActions()
    data class ToggleSelection(val matrixItem: MatrixItem) : RoomJoinRuleChooseRestrictedActions()
    data class SelectJoinRules(val rules: RoomJoinRules) : RoomJoinRuleChooseRestrictedActions()
    object DoUpdateJoinRules : RoomJoinRuleChooseRestrictedActions()
    data class SwitchToRoomAfterMigration(val roomId: String) : RoomJoinRuleChooseRestrictedActions()
}
