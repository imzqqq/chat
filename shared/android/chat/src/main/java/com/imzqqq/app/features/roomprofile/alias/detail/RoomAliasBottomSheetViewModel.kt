package com.imzqqq.app.features.roomprofile.alias.detail

import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.platform.EmptyAction
import com.imzqqq.app.core.platform.EmptyViewEvents
import com.imzqqq.app.core.platform.VectorViewModel
import org.matrix.android.sdk.api.session.Session

class RoomAliasBottomSheetViewModel @AssistedInject constructor(
        @Assisted initialState: RoomAliasBottomSheetState,
        session: Session
) : VectorViewModel<RoomAliasBottomSheetState, EmptyAction, EmptyViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<RoomAliasBottomSheetViewModel, RoomAliasBottomSheetState> {
        override fun create(initialState: RoomAliasBottomSheetState): RoomAliasBottomSheetViewModel
    }

    companion object : MavericksViewModelFactory<RoomAliasBottomSheetViewModel, RoomAliasBottomSheetState> by hiltMavericksViewModelFactory()

    init {
        setState {
            copy(
                    matrixToLink = session.permalinkService().createPermalink(alias)
            )
        }
    }

    override fun handle(action: EmptyAction) {
        // No op
    }
}
