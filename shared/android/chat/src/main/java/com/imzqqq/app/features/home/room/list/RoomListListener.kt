package com.imzqqq.app.features.home.room.list

import org.matrix.android.sdk.api.session.room.model.RoomSummary
import org.matrix.android.sdk.api.session.room.model.SpaceChildInfo

interface RoomListListener {
    fun onRoomClicked(room: RoomSummary)
    fun onRoomLongClicked(room: RoomSummary): Boolean
    fun onRejectRoomInvitation(room: RoomSummary)
    fun onAcceptRoomInvitation(room: RoomSummary)
    fun onJoinSuggestedRoom(room: SpaceChildInfo)
    fun onSuggestedRoomClicked(room: SpaceChildInfo)
}
