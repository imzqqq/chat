package com.imzqqq.app.features.spaces.invite

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class SpaceInviteBottomSheetAction : VectorViewModelAction {
    object DoJoin : SpaceInviteBottomSheetAction()
    object DoReject : SpaceInviteBottomSheetAction()
}
