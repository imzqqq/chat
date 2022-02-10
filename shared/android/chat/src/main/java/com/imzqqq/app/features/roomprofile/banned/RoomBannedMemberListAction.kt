package com.imzqqq.app.features.roomprofile.banned

import com.imzqqq.app.core.platform.VectorViewModelAction
import org.matrix.android.sdk.api.session.room.model.RoomMemberSummary

sealed class RoomBannedMemberListAction : VectorViewModelAction {
    data class QueryInfo(val roomMemberSummary: RoomMemberSummary) : RoomBannedMemberListAction()
    data class UnBanUser(val roomMemberSummary: RoomMemberSummary) : RoomBannedMemberListAction()
    data class Filter(val filter: String) : RoomBannedMemberListAction()
}
