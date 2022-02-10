package com.imzqqq.app.features.spaces.people

import com.imzqqq.app.core.platform.VectorSharedAction

sealed class SpacePeopleSharedAction : VectorSharedAction {
    object Dismiss : SpacePeopleSharedAction()
    object ShowModalLoading : SpacePeopleSharedAction()
    object HideModalLoading : SpacePeopleSharedAction()
    data class NavigateToRoom(val roomId: String) : SpacePeopleSharedAction()
    data class NavigateToInvite(val spaceId: String) : SpacePeopleSharedAction()
}
