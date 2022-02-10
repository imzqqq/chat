package com.imzqqq.app.features.spaces.create

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class CreateSpaceEvents : VectorViewEvents {
    object NavigateToDetails : CreateSpaceEvents()
    object NavigateToChooseType : CreateSpaceEvents()
    object NavigateToAddRooms : CreateSpaceEvents()
    object NavigateToAdd3Pid : CreateSpaceEvents()
    object NavigateToChoosePrivateType : CreateSpaceEvents()
    object Dismiss : CreateSpaceEvents()
    data class FinishSuccess(val spaceId: String, val defaultRoomId: String?, val topology: SpaceTopology?) : CreateSpaceEvents()
    data class ShowModalError(val errorMessage: String) : CreateSpaceEvents()
    object HideModalLoading : CreateSpaceEvents()
    data class ShowModalLoading(val message: String?) : CreateSpaceEvents()
}
