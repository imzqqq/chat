package com.imzqqq.app.features.call.lookup

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RoomVirtualContent(
        @Json(name = "native_room") val nativeRoomId: String
)
