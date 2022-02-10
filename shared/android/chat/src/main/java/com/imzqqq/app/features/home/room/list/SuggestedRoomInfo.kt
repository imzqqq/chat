package com.imzqqq.app.features.home.room.list

import com.airbnb.mvrx.Async
import org.matrix.android.sdk.api.session.room.model.SpaceChildInfo

class SuggestedRoomInfo(
        val rooms: List<SpaceChildInfo>,
        val joinEcho: Map<String, Async<Unit>>
)
