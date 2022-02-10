package com.imzqqq.app.ui.robot.settings

import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.imzqqq.app.R
import com.imzqqq.app.espresso.tools.clickOnPreference
import com.imzqqq.app.espresso.tools.waitUntilViewVisible

class SettingsAdvancedRobot {

    fun crawl() {
        clickOnPreference(R.string.settings_notifications_targets)
        pressBack()

        clickOnPreference(R.string.settings_push_rules)
        pressBack()
    }

    fun toggleDeveloperMode() {
        clickOn(R.string.settings_developer_mode_summary)
    }

    fun crawlDeveloperOptions() {
        clickOnPreference(R.string.settings_account_data)
        waitUntilViewVisible(withText("m.push_rules"))
        clickOn("m.push_rules")
        pressBack()
        pressBack()
        clickOnPreference(R.string.settings_key_requests)
        pressBack()
    }
}
