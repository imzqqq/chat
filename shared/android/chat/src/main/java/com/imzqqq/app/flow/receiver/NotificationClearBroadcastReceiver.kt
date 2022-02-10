package com.imzqqq.app.flow.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.imzqqq.app.flow.components.notifications.NotificationHelper
import com.imzqqq.app.flow.db.AccountManager
import javax.inject.Inject

class NotificationClearBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var accountManager: AccountManager

    override fun onReceive(context: Context, intent: Intent) {
        val accountId = intent.getLongExtra(NotificationHelper.ACCOUNT_ID, -1)

        val account = accountManager.getAccountById(accountId)
        if (account != null) {
            account.activeNotifications = "[]"
            accountManager.saveAccount(account)
        }
    }
}
