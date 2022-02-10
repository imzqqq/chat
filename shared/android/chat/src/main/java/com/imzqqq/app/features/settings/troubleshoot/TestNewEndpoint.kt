package com.imzqqq.app.features.settings.troubleshoot

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import com.imzqqq.app.R
import com.imzqqq.app.core.pushers.UPHelper
import com.imzqqq.app.core.resources.StringProvider
import javax.inject.Inject

/*
* Test that app can successfully retrieve a new endpoint
 */
class TestNewEndpoint @Inject constructor(private val context: FragmentActivity,
                                          private val stringProvider: StringProvider
                                          ) : TroubleshootTest(R.string.settings_troubleshoot_test_endpoint_title) {

    override fun perform(activityResultLauncher: ActivityResultLauncher<Intent>) {
        status = TestStatus.RUNNING

        // Troubleshooters might send a screenshot of this, so use endpoint without token information
        val endpoint = UPHelper.getPrivacyFriendlyUpEndpoint(context)

        if (UPHelper.isEmbeddedDistributor(context)) {
            if (!endpoint.isNullOrEmpty()) {
                status = TestStatus.SUCCESS
                description = stringProvider.getString(R.string.settings_troubleshoot_test_fcm_success, endpoint)
            } else {
                status = TestStatus.FAILED
                description = stringProvider.getString(R.string.settings_troubleshoot_test_fcm_failed_sc)
            }
        } else {
            if (!endpoint.isNullOrEmpty()) {
                status = TestStatus.SUCCESS
                description = stringProvider.getString(R.string.settings_troubleshoot_test_endpoint_success, endpoint)
            } else {
                status = TestStatus.FAILED
                description = stringProvider.getString(R.string.settings_troubleshoot_test_endpoint_failed)
            }
        }
    }
}
