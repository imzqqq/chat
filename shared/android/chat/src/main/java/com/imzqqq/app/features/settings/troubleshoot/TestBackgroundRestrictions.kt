package com.imzqqq.app.features.settings.troubleshoot

import android.content.Intent
import android.net.ConnectivityManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.getSystemService
import androidx.core.net.ConnectivityManagerCompat
import androidx.fragment.app.FragmentActivity
import com.imzqqq.app.R
import com.imzqqq.app.core.resources.StringProvider
import javax.inject.Inject

class TestBackgroundRestrictions @Inject constructor(private val context: FragmentActivity,
                                                     private val stringProvider: StringProvider) :
    TroubleshootTest(R.string.settings_troubleshoot_test_bg_restricted_title) {

    override fun perform(activityResultLauncher: ActivityResultLauncher<Intent>) {
        context.getSystemService<ConnectivityManager>()!!.apply {
            // Checks if the device is on a metered network
            if (isActiveNetworkMetered) {
                // Checks user’s Data Saver settings.
                when (ConnectivityManagerCompat.getRestrictBackgroundStatus(this)) {
                    ConnectivityManagerCompat.RESTRICT_BACKGROUND_STATUS_ENABLED     -> {
                        // Background data usage is blocked for this app. Wherever possible,
                        // the app should also use less data in the foreground.
                        description = stringProvider.getString(R.string.settings_troubleshoot_test_bg_restricted_failed,
                                "RESTRICT_BACKGROUND_STATUS_ENABLED")
                        status = TestStatus.FAILED
                        quickFix = null
                    }
                    ConnectivityManagerCompat.RESTRICT_BACKGROUND_STATUS_WHITELISTED -> {
                        // The app is whitelisted. Wherever possible,
                        // the app should use less data in the foreground and background.
                        description = stringProvider.getString(R.string.settings_troubleshoot_test_bg_restricted_success,
                                "RESTRICT_BACKGROUND_STATUS_WHITELISTED")
                        status = TestStatus.SUCCESS
                        quickFix = null
                    }
                    ConnectivityManagerCompat.RESTRICT_BACKGROUND_STATUS_DISABLED    -> {
                        // Data Saver is disabled. Since the device is connected to a
                        // metered network, the app should use less data wherever possible.
                        description = stringProvider.getString(R.string.settings_troubleshoot_test_bg_restricted_success,
                                "RESTRICT_BACKGROUND_STATUS_DISABLED")
                        status = TestStatus.SUCCESS
                        quickFix = null
                    }
                }
            } else {
                // The device is not on a metered network.
                // Use data as required to perform syncs, downloads, and updates.
                description = stringProvider.getString(R.string.settings_troubleshoot_test_bg_restricted_success, "")
                status = TestStatus.SUCCESS
                quickFix = null
            }
        }
    }
}
