package com.imzqqq.app.features.home

import com.imzqqq.app.core.platform.VectorSharedAction

/**
 * Supported navigation actions for [HomeActivity]
 */
sealed class HomeActivitySharedAction : VectorSharedAction {
    object OpenDrawer : HomeActivitySharedAction()
    object CloseDrawer : HomeActivitySharedAction()
    data class OpenGroup(val clearFragment: Boolean) : HomeActivitySharedAction()
    object AddSpace : HomeActivitySharedAction()
    data class OpenSpacePreview(val spaceId: String) : HomeActivitySharedAction()
    data class OpenSpaceInvite(val spaceId: String) : HomeActivitySharedAction()
    data class ShowSpaceSettings(val spaceId: String) : HomeActivitySharedAction()
    object SendSpaceFeedBack : HomeActivitySharedAction()
}
