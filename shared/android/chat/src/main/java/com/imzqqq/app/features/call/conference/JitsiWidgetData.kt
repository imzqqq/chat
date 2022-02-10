package com.imzqqq.app.features.call.conference

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * This is jitsi widget data
 * https://github.com/matrix-org/matrix-doc/blob/b910b8966524febe7ffe78f723127a5037defe64/api/widgets/definitions/jitsi_data.yaml
 */
@JsonClass(generateAdapter = true)
data class JitsiWidgetData(
        @Json(name = "domain") val domain: String,
        @Json(name = "conferenceId") val confId: String,
        @Json(name = "isAudioOnly") val isAudioOnly: Boolean = false,
        @Json(name = "auth") val auth: String? = null
)
