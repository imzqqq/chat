package com.imzqqq.app.features.call.conference

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JitsiWellKnown(
        @Json(name = "auth") val auth: String
)
