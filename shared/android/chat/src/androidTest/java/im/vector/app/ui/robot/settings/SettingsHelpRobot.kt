package com.imzqqq.app.ui.robot.settings

import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaDialogInteractions.clickDialogPositiveButton
import com.imzqqq.app.R

class SettingsHelpRobot {

    fun crawl() {
        /*
        clickOn(R.string.settings_app_info_link_title)
        Cannot go back...
        pressBack()
        clickOn(R.string.settings_copyright)
        pressBack()
        clickOn(R.string.settings_app_term_conditions)
        pressBack()
        clickOn(R.string.settings_privacy_policy)
        pressBack()
         */
        clickOn(R.string.settings_third_party_notices)
        clickDialogPositiveButton()
    }
}
