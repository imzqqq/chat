package com.imzqqq.app.features.roomprofile.alias.detail

import com.airbnb.mvrx.MavericksState

data class RoomAliasBottomSheetState(
        val alias: String,
        val matrixToLink: String? = null,
        val isPublished: Boolean,
        val isMainAlias: Boolean,
        val isLocal: Boolean,
        val canEditCanonicalAlias: Boolean
) : MavericksState {

    constructor(args: RoomAliasBottomSheetArgs) : this(
            alias = args.alias,
            isPublished = args.isPublished,
            isMainAlias = args.isMainAlias,
            isLocal = args.isLocal,
            canEditCanonicalAlias = args.canEditCanonicalAlias
    )
}
