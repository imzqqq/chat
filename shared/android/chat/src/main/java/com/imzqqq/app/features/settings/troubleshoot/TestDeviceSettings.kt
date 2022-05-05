package com.imzqqq.app.features.settings.troubleshoot

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.imzqqq.app.R
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.features.settings.VectorPreferences
import javax.inject.Inject

/**
 * Checks if notifications are enable in the system settings for this app.
 */
class TestDeviceSettings @Inject constructor(private val vectorPreferences: VectorPreferences,
                                             private val stringProvider: StringProvider) :
    TroubleshootTest(R.string.settings_troubleshoot_test_device_settings_title) {

    override fun perform(activityResultLauncher: ActivityResultLauncher<Intent>) {
        if (vectorPreferences.areNotificationEnabledForDevice()) {
            description = stringProvider.getString(R.string.settings_troubleshoot_test_device_settings_success)
            quickFix = null
            status = TestStatus.SUCCESS
        } else {
            quickFix = object : TroubleshootQuickFix(R.string.settings_troubleshoot_test_device_settings_quickfix) {
                override fun doFix() {
                    vectorPreferences.setNotificationEnabledForDevice(true)
                    manager?.retry(activityResultLauncher)
                }
            }
            description = stringProvider.getString(R.string.settings_troubleshoot_test_device_settings_failed)
            status = TestStatus.FAILED
        }
    }
}
