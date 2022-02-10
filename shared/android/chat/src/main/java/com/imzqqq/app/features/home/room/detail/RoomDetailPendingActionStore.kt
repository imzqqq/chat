package com.imzqqq.app.features.home.room.detail

import com.imzqqq.app.core.utils.TemporaryStore
import javax.inject.Inject
import javax.inject.Singleton

// Store to keep a pending action from sub screen of a room detail
@Singleton
class RoomDetailPendingActionStore @Inject constructor() : TemporaryStore<RoomDetailPendingAction>(10_000)
