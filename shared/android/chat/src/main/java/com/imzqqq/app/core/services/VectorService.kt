package com.imzqqq.app.core.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import timber.log.Timber

/**
 * Parent class for all services
 */
abstract class VectorService : Service() {

    /**
     * Tells if the service self destroyed.
     */
    private var mIsSelfDestroyed = false

    override fun onCreate() {
        super.onCreate()

        Timber.i("## onCreate() : $this")
    }

    override fun onDestroy() {
        Timber.i("## onDestroy() : $this")

        if (!mIsSelfDestroyed) {
            Timber.w("## Destroy by the system : $this")
        }

        super.onDestroy()
    }

    protected fun myStopSelf() {
        mIsSelfDestroyed = true
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
