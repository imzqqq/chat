package com.imzqqq.app.features.settings.troubleshoot

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import com.imzqqq.app.R
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.utils.isIgnoringBatteryOptimizations
import com.imzqqq.app.core.utils.requestDisablingBatteryOptimization
import javax.inject.Inject

class TestBatteryOptimization @Inject constructor(
        private val context: FragmentActivity,
        private val stringProvider: StringProvider
) : TroubleshootTest(R.string.settings_troubleshoot_test_battery_title) {

    override fun perform(activityResultLauncher: ActivityResultLauncher<Intent>) {
        if (isIgnoringBatteryOptimizations(context)) {
            description = stringProvider.getString(R.string.settings_troubleshoot_test_battery_success)
            status = TestStatus.SUCCESS
            quickFix = null
        } else {
            description = stringProvider.getString(R.string.settings_troubleshoot_test_battery_failed)
            quickFix = object : TroubleshootQuickFix(R.string.settings_troubleshoot_test_battery_quickfix) {
                override fun doFix() {
                    requestDisablingBatteryOptimization(context, activityResultLauncher)
                }
            }
            status = TestStatus.FAILED
        }
    }
}
