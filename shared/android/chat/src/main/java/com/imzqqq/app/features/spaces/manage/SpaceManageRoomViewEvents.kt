package com.imzqqq.app.features.spaces.manage

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class SpaceManageRoomViewEvents : VectorViewEvents {
//    object BulkActionSuccess: SpaceManageRoomViewEvents()
    data class BulkActionFailure(val errorList: List<Throwable>) : SpaceManageRoomViewEvents()
}
