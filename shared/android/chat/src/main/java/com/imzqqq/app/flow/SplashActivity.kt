package com.imzqqq.app.flow

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.flow.components.notifications.NotificationHelper
import com.imzqqq.app.flow.db.AccountManager
import javax.inject.Inject

/*
 * MARK - imzqqq, TODO: this is deprecated, and we launch flow in Chat page now
 * @Deprecated
 */
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject lateinit var accountManager: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** delete old notification channels */
        NotificationHelper.deleteLegacyNotificationChannels(this, accountManager)

        /** Determine whether the user is currently logged in, and if so go ahead and load the
         *  timeline. Otherwise, start the activity_login screen. */

        val intent = if (accountManager.activeAccount != null) {
            Intent(this, FlowActivity::class.java)
        } else {
            FlowLoginActivity.getIntent(this, false)
        }
        startActivity(intent)
        finish()
    }
}

