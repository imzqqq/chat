package com.imzqqq.app.features.spaces

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class SpaceLeaveViewAction : VectorViewModelAction {
    object SetAutoLeaveAll : SpaceLeaveViewAction()
    object SetAutoLeaveNone : SpaceLeaveViewAction()
    object SetAutoLeaveSelected : SpaceLeaveViewAction()
    object LeaveSpace : SpaceLeaveViewAction()
}
