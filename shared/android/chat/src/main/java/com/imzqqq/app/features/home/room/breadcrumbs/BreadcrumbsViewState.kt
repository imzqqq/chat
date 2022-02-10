package com.imzqqq.app.features.home.room.breadcrumbs

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import org.matrix.android.sdk.api.session.room.model.RoomSummary

data class BreadcrumbsViewState(
        val asyncBreadcrumbs: Async<List<RoomSummary>> = Uninitialized
) : MavericksState
