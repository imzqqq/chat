package com.imzqqq.app.features.spaces.manage

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class SpaceAddRoomsViewEvents : VectorViewEvents {
    object WarnUnsavedChanged : SpaceAddRoomsViewEvents()
    object SavedDone : SpaceAddRoomsViewEvents()
    data class SaveFailed(val reason: Throwable) : SpaceAddRoomsViewEvents()
}
