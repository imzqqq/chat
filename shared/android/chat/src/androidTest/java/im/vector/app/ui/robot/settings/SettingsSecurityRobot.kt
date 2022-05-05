package com.imzqqq.app.ui.robot.settings

import androidx.test.espresso.Espresso
import com.imzqqq.app.R
import com.imzqqq.app.espresso.tools.clickOnPreference

class SettingsSecurityRobot {

    fun crawl() {
        clickOnPreference(R.string.settings_active_sessions_show_all)
        Espresso.pressBack()

        clickOnPreference(R.string.encryption_message_recovery)
        // TODO go deeper here
        Espresso.pressBack()
        /* Cannot exit
        clickOnPreference(R.string.encryption_export_e2e_room_keys)
        pressBack()
         */
    }
}
