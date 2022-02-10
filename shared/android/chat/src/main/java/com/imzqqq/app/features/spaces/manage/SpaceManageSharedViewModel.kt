package com.imzqqq.app.features.spaces.manage

import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.extensions.exhaustive
import com.imzqqq.app.core.platform.VectorViewModel
import org.matrix.android.sdk.api.session.Session

class SpaceManageSharedViewModel @AssistedInject constructor(
        @Assisted initialState: SpaceManageViewState,
        private val session: Session
) : VectorViewModel<SpaceManageViewState, SpaceManagedSharedAction, SpaceManagedSharedViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<SpaceManageSharedViewModel, SpaceManageViewState> {
        override fun create(initialState: SpaceManageViewState): SpaceManageSharedViewModel
    }

    companion object : MavericksViewModelFactory<SpaceManageSharedViewModel, SpaceManageViewState> by hiltMavericksViewModelFactory()

    override fun handle(action: SpaceManagedSharedAction) {
        when (action) {
            SpaceManagedSharedAction.HandleBack                  -> {
                // for now finish
                _viewEvents.post(SpaceManagedSharedViewEvents.Finish)
            }
            SpaceManagedSharedAction.HideLoading                 -> _viewEvents.post(SpaceManagedSharedViewEvents.HideLoading)
            SpaceManagedSharedAction.ShowLoading                 -> _viewEvents.post(SpaceManagedSharedViewEvents.ShowLoading)
            SpaceManagedSharedAction.CreateRoom                  -> _viewEvents.post(SpaceManagedSharedViewEvents.NavigateToCreateRoom)
            SpaceManagedSharedAction.CreateSpace                 -> _viewEvents.post(SpaceManagedSharedViewEvents.NavigateToCreateSpace)
            SpaceManagedSharedAction.ManageRooms                 -> _viewEvents.post(SpaceManagedSharedViewEvents.NavigateToManageRooms)
            SpaceManagedSharedAction.OpenSpaceAliasesSettings    -> _viewEvents.post(SpaceManagedSharedViewEvents.NavigateToAliasSettings)
            SpaceManagedSharedAction.OpenSpacePermissionSettings -> _viewEvents.post(SpaceManagedSharedViewEvents.NavigateToPermissionSettings)
        }.exhaustive
    }
}
