package com.imzqqq.app.features.spaces.leave

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class SpaceLeaveAdvanceViewAction : VectorViewModelAction {
    data class ToggleSelection(val roomId: String) : SpaceLeaveAdvanceViewAction()
    data class UpdateFilter(val filter: String) : SpaceLeaveAdvanceViewAction()
    object DoLeave : SpaceLeaveAdvanceViewAction()
    object ClearError : SpaceLeaveAdvanceViewAction()
}
