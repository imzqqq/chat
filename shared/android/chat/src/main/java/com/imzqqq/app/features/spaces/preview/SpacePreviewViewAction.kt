package com.imzqqq.app.features.spaces.preview

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class SpacePreviewViewAction : VectorViewModelAction {
    object ViewReady : SpacePreviewViewAction()
    object AcceptInvite : SpacePreviewViewAction()
    object DismissInvite : SpacePreviewViewAction()
}
