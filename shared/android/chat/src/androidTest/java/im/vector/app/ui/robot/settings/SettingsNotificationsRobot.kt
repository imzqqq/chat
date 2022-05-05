package com.imzqqq.app.ui.robot.settings

import androidx.test.espresso.Espresso.pressBack
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.imzqqq.app.BuildConfig
import com.imzqqq.app.R
import com.imzqqq.app.espresso.tools.clickOnPreference

class SettingsNotificationsRobot {

    fun crawl() {
        if (BuildConfig.USE_NOTIFICATION_SETTINGS_V2) {
            clickOn(R.string.settings_notification_default)
            pressBack()
            clickOn(R.string.settings_notification_mentions_and_keywords)
            // TODO Test adding a keyword?
            pressBack()
            clickOn(R.string.settings_notification_other)
            pressBack()
        } else {
            clickOn(R.string.settings_notification_advanced)
            pressBack()
        }
        /*
        clickOn(R.string.settings_noisy_notifications_preferences)
        TODO Cannot go back
        pressBack()
        clickOn(R.string.settings_silent_notifications_preferences)
        pressBack()
        clickOn(R.string.settings_call_notifications_preferences)
        pressBack()
         */
        clickOnPreference(R.string.settings_notification_troubleshoot)
        pressBack()
    }
}
