package com.imzqqq.app.core.platform

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.imzqqq.app.core.utils.DataSource
import com.imzqqq.app.core.utils.PublishDataSource

abstract class VectorViewModel<S : MavericksState, VA : VectorViewModelAction, VE : VectorViewEvents>(initialState: S) :
        MavericksViewModel<S>(initialState) {

    // Used to post transient events to the View
    protected val _viewEvents = PublishDataSource<VE>()
    val viewEvents: DataSource<VE> = _viewEvents

    abstract fun handle(action: VA)
}
