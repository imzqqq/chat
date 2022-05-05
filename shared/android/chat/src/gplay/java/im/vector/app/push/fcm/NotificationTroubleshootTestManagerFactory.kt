package com.imzqqq.app.push.fcm

import androidx.fragment.app.Fragment
import com.imzqqq.app.core.pushers.UPHelper
import com.imzqqq.app.features.settings.troubleshoot.NotificationTroubleshootTestManager
import com.imzqqq.app.features.settings.troubleshoot.TestAccountSettings
import com.imzqqq.app.features.settings.troubleshoot.TestAutoStartBoot
import com.imzqqq.app.features.settings.troubleshoot.TestBackgroundRestrictions
import com.imzqqq.app.features.settings.troubleshoot.TestBatteryOptimization
import com.imzqqq.app.features.settings.troubleshoot.TestDeviceSettings
import com.imzqqq.app.features.settings.troubleshoot.TestNotification
import com.imzqqq.app.features.settings.troubleshoot.TestPushRulesSettings
import com.imzqqq.app.features.settings.troubleshoot.TestSystemSettings
import com.imzqqq.app.features.settings.troubleshoot.TestNewEndpoint
import com.imzqqq.app.gplay.features.settings.troubleshoot.TestPlayServices
import com.imzqqq.app.features.settings.troubleshoot.TestPushFromPushGateway
import com.imzqqq.app.features.settings.troubleshoot.TestTokenRegistration
import javax.inject.Inject

class NotificationTroubleshootTestManagerFactory @Inject constructor(
        private val testSystemSettings: TestSystemSettings,
        private val testAccountSettings: TestAccountSettings,
        private val testDeviceSettings: TestDeviceSettings,
        private val testPlayServices: TestPlayServices,
        private val testNewEndpoint: TestNewEndpoint,
        private val testTokenRegistration: TestTokenRegistration,
        private val testPushFromPushGateway: TestPushFromPushGateway,
        private val testPushRulesSettings: TestPushRulesSettings,
        private val testAutoStartBoot: TestAutoStartBoot,
        private val testBackgroundRestrictions: TestBackgroundRestrictions,
        private val testBatteryOptimization: TestBatteryOptimization,
        private val testNotification: TestNotification
) {

    fun create(fragment: Fragment): NotificationTroubleshootTestManager {
        val mgr = NotificationTroubleshootTestManager(fragment)
        mgr.addTest(testSystemSettings)
        mgr.addTest(testAccountSettings)
        mgr.addTest(testDeviceSettings)
        mgr.addTest(testPushRulesSettings)
        if (UPHelper.distributorExists(fragment.requireContext())) {
            mgr.addTest(testPlayServices)
            mgr.addTest(testNewEndpoint)
            mgr.addTest(testTokenRegistration)
            mgr.addTest(testPushFromPushGateway)
        } else {
            mgr.addTest(testAutoStartBoot)
            mgr.addTest(testBackgroundRestrictions)
            mgr.addTest(testBatteryOptimization)
        }
        mgr.addTest(testNotification)
        return mgr
    }
}
