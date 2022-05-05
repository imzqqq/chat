package com.imzqqq.app.features.spaces

import com.imzqqq.app.core.platform.VectorViewModelAction
import org.matrix.android.sdk.api.session.group.model.GroupSummary
import org.matrix.android.sdk.api.session.room.model.RoomSummary

sealed class SpaceListAction : VectorViewModelAction {
    data class SelectSpace(val spaceSummary: RoomSummary?) : SpaceListAction()
    data class OpenSpaceInvite(val spaceSummary: RoomSummary) : SpaceListAction()
    data class LeaveSpace(val spaceSummary: RoomSummary) : SpaceListAction()
    data class ToggleExpand(val spaceSummary: RoomSummary) : SpaceListAction()
    object AddSpace : SpaceListAction()
    data class MoveSpace(val spaceId: String, val delta: Int) : SpaceListAction()
    data class OnStartDragging(val spaceId: String, val expanded: Boolean) : SpaceListAction()
    data class OnEndDragging(val spaceId: String, val expanded: Boolean) : SpaceListAction()

    data class SelectLegacyGroup(val groupSummary: GroupSummary?) : SpaceListAction()
}
