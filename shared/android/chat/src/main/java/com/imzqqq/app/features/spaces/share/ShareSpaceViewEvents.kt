package com.imzqqq.app.features.spaces.share

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class ShareSpaceViewEvents : VectorViewEvents {
    data class NavigateToInviteUser(val spaceId: String) : ShareSpaceViewEvents()
    data class ShowInviteByLink(val permalink: String, val spaceName: String) : ShareSpaceViewEvents()
}
