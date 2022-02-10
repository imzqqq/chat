package com.imzqqq.app.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.imzqqq.app.core.extensions.singletonEntryPoint
import timber.log.Timber

class OnApplicationUpgradeOrRebootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Timber.v("## onReceive() ${intent.action}")
        val singletonEntryPoint = context.singletonEntryPoint()
            BackgroundSyncStarter.start(
                    context,
                    singletonEntryPoint.vectorPreferences(),
                    singletonEntryPoint.activeSessionHolder()
            )
    }
}
