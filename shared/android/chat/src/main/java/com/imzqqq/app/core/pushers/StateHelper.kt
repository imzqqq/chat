package com.imzqqq.app.core.pushers

import android.content.Context
import com.imzqqq.app.core.di.ActiveSessionHolder
import com.imzqqq.app.core.receiver.AlarmSyncBroadcastReceiver
import com.imzqqq.app.core.receiver.BackgroundSyncStarter
import com.imzqqq.app.features.settings.VectorPreferences

object StateHelper {
    fun onEnterForeground(context: Context, activeSessionHolder: ActiveSessionHolder) {
        // try to stop all regardless of background mode
        activeSessionHolder.getSafeActiveSession()?.stopAnyBackgroundSync()
        AlarmSyncBroadcastReceiver.cancelAlarm(context)
    }

    fun onEnterBackground(context: Context, vectorPreferences: VectorPreferences, activeSessionHolder: ActiveSessionHolder) {
        BackgroundSyncStarter.start(context, vectorPreferences, activeSessionHolder)
    }
}
