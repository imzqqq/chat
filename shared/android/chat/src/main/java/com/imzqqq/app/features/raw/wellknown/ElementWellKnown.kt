package com.imzqqq.app.features.raw.wellknown

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ElementWellKnown(
        /**
         * Preferred Jitsi domain
         */
        @Json(name = "im.vector.riot.jitsi")
        val jitsiServer: WellKnownPreferredConfig? = null,

        /**
         * The settings above were first proposed under a im.vector.riot.e2ee key, which is now deprecated.
         * Element will check for either key, preferring io.element.e2ee if both exist.
         */
        @Json(name = "io.element.e2ee")
        val elementE2E: E2EWellKnownConfig? = null,

        @Json(name = "im.vector.riot.e2ee")
        val riotE2E: E2EWellKnownConfig? = null
)

@JsonClass(generateAdapter = true)
data class E2EWellKnownConfig(
        /**
         * Option to allow homeserver admins to set the default E2EE behaviour back to disabled for DMs / private rooms
         * (as it was before) for various environments where this is desired.
         */
        @Json(name = "default")
        val e2eDefault: Boolean? = null
)

@JsonClass(generateAdapter = true)
data class WellKnownPreferredConfig(
        @Json(name = "preferredDomain")
        val preferredDomain: String? = null
)
