package com.imzqqq.app.core.receiver

import android.content.Context
import com.imzqqq.app.core.di.ActiveSessionHolder
import com.imzqqq.app.features.settings.BackgroundSyncMode
import com.imzqqq.app.features.settings.VectorPreferences
import timber.log.Timber

object BackgroundSyncStarter {
    fun start(context: Context, vectorPreferences: VectorPreferences, activeSessionHolder: ActiveSessionHolder) {
        if (vectorPreferences.areNotificationEnabledForDevice()) {
            val activeSession = activeSessionHolder.getSafeActiveSession() ?: return
            when (vectorPreferences.getFdroidSyncBackgroundMode()) {
                BackgroundSyncMode.FDROID_BACKGROUND_SYNC_MODE_FOR_BATTERY  -> {
                    // we rely on periodic worker
                    Timber.i("## Sync: Work scheduled to periodically sync in ${vectorPreferences.backgroundSyncDelay()}s")
                    activeSession.startAutomaticBackgroundSync(
                            vectorPreferences.backgroundSyncTimeOut().toLong(),
                            vectorPreferences.backgroundSyncDelay().toLong()
                    )
                }
                BackgroundSyncMode.FDROID_BACKGROUND_SYNC_MODE_FOR_REALTIME -> {
                    // We need to use alarm in this mode
                    AlarmSyncBroadcastReceiver.scheduleAlarm(context, activeSession.sessionId, vectorPreferences.backgroundSyncDelay())
                    Timber.i("## Sync: Alarm scheduled to start syncing")
                }
                BackgroundSyncMode.FDROID_BACKGROUND_SYNC_MODE_DISABLED     -> {
                    // we do nothing
                    Timber.i("## Sync: background sync is disabled")
                }
            }
        }
    }
}
