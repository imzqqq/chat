package com.imzqqq.app.features.notifications

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import timber.log.Timber
import javax.inject.Inject

class NotificationDisplayer @Inject constructor(context: Context) {

    private val notificationManager = NotificationManagerCompat.from(context)

    fun showNotificationMessage(tag: String?, id: Int, notification: Notification) {
        notificationManager.notify(tag, id, notification)
    }

    fun cancelNotificationMessage(tag: String?, id: Int) {
        notificationManager.cancel(tag, id)
    }

    fun cancelAllNotifications() {
        // Keep this try catch (reported by GA)
        try {
            notificationManager.cancelAll()
        } catch (e: Exception) {
            Timber.e(e, "## cancelAllNotifications() failed")
        }
    }
}
