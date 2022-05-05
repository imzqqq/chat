@file:Suppress("UNUSED_PARAMETER")

package com.imzqqq.app.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

/**
 * No Op version
 */
class DebugReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // No op
    }

    companion object {
        fun getIntentFilter(context: Context) = IntentFilter()
    }
}
