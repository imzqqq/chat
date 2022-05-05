package com.imzqqq.app.features.settings.troubleshoot

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.FragmentActivity
import com.imzqqq.app.R
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.utils.startNotificationSettingsIntent
import javax.inject.Inject

/**
 * Checks if notifications are enable in the system settings for this app.
 */
class TestSystemSettings @Inject constructor(private val context: FragmentActivity,
                                             private val stringProvider: StringProvider) :
    TroubleshootTest(R.string.settings_troubleshoot_test_system_settings_title) {

    override fun perform(activityResultLauncher: ActivityResultLauncher<Intent>) {
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            description = stringProvider.getString(R.string.settings_troubleshoot_test_system_settings_success)
            quickFix = null
            status = TestStatus.SUCCESS
        } else {
            description = stringProvider.getString(R.string.settings_troubleshoot_test_system_settings_failed)
            quickFix = object : TroubleshootQuickFix(R.string.open_settings) {
                override fun doFix() {
                    startNotificationSettingsIntent(context, activityResultLauncher)
                }
            }
            status = TestStatus.FAILED
        }
    }
}
