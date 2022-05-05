package com.imzqqq.app.features.roomdirectory.picker

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.imzqqq.app.features.roomdirectory.RoomDirectoryServer
import org.matrix.android.sdk.api.session.room.model.thirdparty.ThirdPartyProtocol

data class RoomDirectoryPickerViewState(
        val asyncThirdPartyRequest: Async<Map<String, ThirdPartyProtocol>> = Uninitialized,
        val customHomeservers: Set<String> = emptySet(),
        val inEditMode: Boolean = false,
        val enteredServer: String = "",
        val addServerAsync: Async<Unit> = Uninitialized,
        // computed
        val directories: List<RoomDirectoryServer> = emptyList()
) : MavericksState
