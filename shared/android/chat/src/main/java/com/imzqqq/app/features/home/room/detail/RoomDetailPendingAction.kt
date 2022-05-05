package com.imzqqq.app.features.home.room.detail

sealed class RoomDetailPendingAction {
    data class OpenOrCreateDm(val userId: String) : RoomDetailPendingAction()
    data class JumpToReadReceipt(val userId: String) : RoomDetailPendingAction()
    data class MentionUser(val userId: String) : RoomDetailPendingAction()
    data class OpenRoom(val roomId: String, val closeCurrentRoom: Boolean = false) : RoomDetailPendingAction()
}
