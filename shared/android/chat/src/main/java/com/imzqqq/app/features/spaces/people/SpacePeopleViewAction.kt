package com.imzqqq.app.features.spaces.people

import com.imzqqq.app.core.platform.VectorViewModelAction
import org.matrix.android.sdk.api.session.room.model.RoomMemberSummary

sealed class SpacePeopleViewAction : VectorViewModelAction {
    data class ChatWith(val member: RoomMemberSummary) : SpacePeopleViewAction()
    object InviteToSpace : SpacePeopleViewAction()
}
