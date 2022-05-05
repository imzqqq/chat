package com.imzqqq.app.features.spaces.preview

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized

data class SpacePreviewState(
        val idOrAlias: String,
        val name: String?  = null,
        val topic: String?  = null,
        val avatarUrl: String? = null,
        val spaceInfo: Async<ChildInfo> = Uninitialized,
        val childInfoList: Async<List<ChildInfo>> = Uninitialized,
        val inviteTermination: Async<Unit> = Uninitialized
) : MavericksState {
    constructor(args: SpacePreviewArgs) : this(idOrAlias = args.idOrAlias)
}

data class ChildInfo(
        val roomId: String,
        val avatarUrl: String?,
        val name: String?,
        val topic: String?,
        val memberCount: Int?,
        val isSubSpace: Boolean?,
        val viaServers: List<String>?,
        val children: Async<List<ChildInfo>>
)
