package com.imzqqq.app.features.spaces.manage

import com.airbnb.mvrx.MavericksState

enum class ManageType {
    AddRooms,
    AddRoomsOnlySpaces,
    Settings,
    ManageRooms
}
data class SpaceManageViewState(
        val spaceId: String = "",
        val manageType: ManageType
) : MavericksState {
    constructor(args: SpaceManageArgs) : this(
            spaceId = args.spaceId,
            manageType = args.manageType
    )
}
