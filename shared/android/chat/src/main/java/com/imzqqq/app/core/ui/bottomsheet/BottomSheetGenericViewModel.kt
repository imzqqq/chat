package com.imzqqq.app.core.ui.bottomsheet

import com.airbnb.mvrx.MavericksState
import com.imzqqq.app.core.platform.EmptyAction
import com.imzqqq.app.core.platform.EmptyViewEvents
import com.imzqqq.app.core.platform.VectorViewModel

abstract class BottomSheetGenericViewModel<State : MavericksState>(initialState: State) :
        VectorViewModel<State, EmptyAction, EmptyViewEvents>(initialState) {

    override fun handle(action: EmptyAction) {
        // No op
    }
}
