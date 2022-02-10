package com.imzqqq.app.features.spaces.explore

import com.imzqqq.app.core.platform.VectorViewModelAction
import org.matrix.android.sdk.api.session.room.model.SpaceChildInfo

sealed class SpaceDirectoryViewAction : VectorViewModelAction {
    data class ExploreSubSpace(val spaceChildInfo: SpaceChildInfo) : SpaceDirectoryViewAction()
    data class JoinOrOpen(val spaceChildInfo: SpaceChildInfo) : SpaceDirectoryViewAction()
    data class ShowDetails(val spaceChildInfo: SpaceChildInfo) : SpaceDirectoryViewAction()
    data class NavigateToRoom(val roomId: String) : SpaceDirectoryViewAction()
    object HandleBack : SpaceDirectoryViewAction()
    object Retry : SpaceDirectoryViewAction()
    object LoadAdditionalItemsIfNeeded : SpaceDirectoryViewAction()
}
