package com.imzqqq.app.features.spaces.explore

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class SpaceDirectoryViewEvents : VectorViewEvents {
    object Dismiss : SpaceDirectoryViewEvents()
    data class NavigateToRoom(val roomId: String) : SpaceDirectoryViewEvents()
    data class NavigateToMxToBottomSheet(val link: String) : SpaceDirectoryViewEvents()
}
