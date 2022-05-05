package org.matrix.android.sdk.api.session.presence.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class PresenceEnum(val value: String) {
    @Json(name = "online")
    ONLINE("online"),

    @Json(name = "offline")
    OFFLINE("offline"),

    @Json(name = "unavailable")
    UNAVAILABLE("unavailable");

    companion object {
        fun from(s: String): PresenceEnum? = values().find { it.value == s }
    }
}
