package com.imzqqq.app.features.spaces

import com.imzqqq.app.core.platform.VectorViewEvents

/**
 * Transient events for group list screen
 */
sealed class SpaceListViewEvents : VectorViewEvents {
    data class OpenSpace(val groupingMethodHasChanged: Boolean) : SpaceListViewEvents()
    data class OpenSpaceSummary(val id: String) : SpaceListViewEvents()
    data class OpenSpaceInvite(val id: String) : SpaceListViewEvents()
    object AddSpace : SpaceListViewEvents()
    data class OpenGroup(val groupingMethodHasChanged: Boolean) : SpaceListViewEvents()
}
