package com.imzqqq.app.flow.components.notifications

import android.content.Context
import dagger.Provides
import com.imzqqq.app.flow.db.AccountEntity
import com.imzqqq.app.flow.entity.Notification

/**
 * Shows notifications.
 */
interface Notifier {
    fun show(notification: Notification, account: AccountEntity, isFirstInBatch: Boolean)
}

class SystemNotifier(
    private val context: Context
) : Notifier {
    override fun show(notification: Notification, account: AccountEntity, isFirstInBatch: Boolean) {
        NotificationHelper.make(context, notification, account, isFirstInBatch)
    }
}
