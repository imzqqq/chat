package com.imzqqq.app.features.spaces.share

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class ShareSpaceAction : VectorViewModelAction {
    object InviteByMxId : ShareSpaceAction()
    object InviteByLink : ShareSpaceAction()
}
