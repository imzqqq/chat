package com.imzqqq.app.core.services

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.imzqqq.app.core.pushers.UPHelper
import com.imzqqq.app.features.settings.VectorPreferences
import timber.log.Timber
import javax.inject.Inject

class GuardServiceStarter @Inject constructor(
        private val preferences: VectorPreferences,
        private val appContext: Context
        ) {
    fun start() {
        if (preferences.isBackgroundSyncEnabled() && UPHelper.allowBackgroundSync(appContext)) {
            try {
                Timber.i("## Sync: starting GuardService")
                val intent = Intent(appContext, GuardService::class.java)
                ContextCompat.startForegroundService(appContext, intent)
            } catch (ex: Throwable) {
                Timber.e("## Sync: ERROR starting GuardService")
            }
        }
    }

    fun stop() {
        val intent = Intent(appContext, GuardService::class.java)
        appContext.stopService(intent)
    }
}
