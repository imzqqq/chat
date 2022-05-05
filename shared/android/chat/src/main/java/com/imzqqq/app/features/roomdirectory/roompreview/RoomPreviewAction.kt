package com.imzqqq.app.features.roomdirectory.roompreview

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class RoomPreviewAction : VectorViewModelAction {
    object Join : RoomPreviewAction()
    object JoinThirdParty : RoomPreviewAction()
}
