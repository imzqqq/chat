package com.imzqqq.app.features.spaces.invite

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class SpaceInviteBottomSheetEvents : VectorViewEvents {
    data class ShowError(val message: String) : SpaceInviteBottomSheetEvents()
}
