package com.imzqqq.app.features.spaces

import com.imzqqq.app.core.platform.VectorSharedAction

sealed class SpacePreviewSharedAction : VectorSharedAction {
    object DismissAction : SpacePreviewSharedAction()
    object ShowModalLoading : SpacePreviewSharedAction()
    object HideModalLoading : SpacePreviewSharedAction()
    data class ShowErrorMessage(val error: String? = null) : SpacePreviewSharedAction()
}
