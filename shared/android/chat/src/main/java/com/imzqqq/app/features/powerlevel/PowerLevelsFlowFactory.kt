package com.imzqqq.app.features.powerlevel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.matrix.android.sdk.api.query.QueryStringValue
import org.matrix.android.sdk.api.session.events.model.EventType
import org.matrix.android.sdk.api.session.events.model.toModel
import org.matrix.android.sdk.api.session.room.Room
import org.matrix.android.sdk.api.session.room.model.PowerLevelsContent
import org.matrix.android.sdk.flow.flow
import org.matrix.android.sdk.flow.mapOptional
import org.matrix.android.sdk.flow.unwrap

class PowerLevelsFlowFactory(private val room: Room) {

    fun createFlow(): Flow<PowerLevelsContent> {
        return room.flow()
                .liveStateEvent(EventType.STATE_ROOM_POWER_LEVELS, QueryStringValue.NoCondition)
                .mapOptional { it.content.toModel<PowerLevelsContent>() }
                .flowOn(Dispatchers.Default)
                .unwrap()
    }
}
