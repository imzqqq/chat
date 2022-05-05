package com.imzqqq.app.features.login

import org.matrix.android.sdk.api.auth.data.HomeServerConnectionConfig
import timber.log.Timber
import javax.inject.Inject

class HomeServerConnectionConfigFactory @Inject constructor() {

    fun create(url: String?): HomeServerConnectionConfig? {
        if (url == null) {
            return null
        }

        return try {
            HomeServerConnectionConfig.Builder()
                    .withHomeServerUri(url)
                    .build()
        } catch (t: Throwable) {
            Timber.e(t)
            null
        }
    }
}
