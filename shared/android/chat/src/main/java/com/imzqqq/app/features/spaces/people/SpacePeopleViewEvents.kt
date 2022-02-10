package com.imzqqq.app.features.spaces.people

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class SpacePeopleViewEvents : VectorViewEvents {
    data class OpenRoom(val roomId: String) : SpacePeopleViewEvents()
    data class InviteToSpace(val spaceId: String) : SpacePeopleViewEvents()
}
