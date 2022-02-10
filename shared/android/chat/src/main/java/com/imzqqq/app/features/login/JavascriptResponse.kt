package com.imzqqq.app.features.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.matrix.android.sdk.api.auth.data.Credentials

@JsonClass(generateAdapter = true)
data class JavascriptResponse(
        @Json(name = "action")
        val action: String? = null,

        /**
         * Use for captcha result
         */
        @Json(name = "response")
        val response: String? = null,

        /**
         * Used for login/registration result
         */
        @Json(name = "credentials")
        val credentials: Credentials? = null
)
