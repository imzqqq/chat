package com.keylesspalace.flow.components.notifications

import android.content.Context
import com.keylesspalace.flow.db.AccountEntity
import com.keylesspalace.flow.entity.Notification

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
