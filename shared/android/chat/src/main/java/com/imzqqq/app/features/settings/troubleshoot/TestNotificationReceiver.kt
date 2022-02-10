package com.imzqqq.app.features.settings.troubleshoot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class TestNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Internal broadcast to any one interested
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }
}
