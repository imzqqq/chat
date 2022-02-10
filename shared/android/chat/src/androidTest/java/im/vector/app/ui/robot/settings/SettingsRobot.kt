package com.imzqqq.app.ui.robot.settings

import com.imzqqq.app.R
import com.imzqqq.app.clickOnAndGoBack

class SettingsRobot {

    fun toggleDeveloperMode() {
        advancedSettings {
            toggleDeveloperMode()
        }
    }

    fun general(block: SettingsGeneralRobot.() -> Unit) {
        clickOnAndGoBack(R.string.settings_general_title) { block(SettingsGeneralRobot()) }
    }

    fun notifications(block: SettingsNotificationsRobot.() -> Unit) {
        clickOnAndGoBack(R.string.settings_notifications) { block(SettingsNotificationsRobot()) }
    }

    fun preferences(block: SettingsPreferencesRobot.() -> Unit) {
        clickOnAndGoBack(R.string.settings_preferences) { block(SettingsPreferencesRobot()) }
    }

    fun voiceAndVideo(block: () -> Unit = {}) {
        clickOnAndGoBack(R.string.preference_voice_and_video) { block() }
    }

    fun ignoredUsers(block: () -> Unit = {}) {
        clickOnAndGoBack(R.string.settings_ignored_users) { block() }
    }

    fun securityAndPrivacy(block: SettingsSecurityRobot.() -> Unit) {
        clickOnAndGoBack(R.string.settings_security_and_privacy) { block(SettingsSecurityRobot()) }
    }

    fun labs(block: () -> Unit = {}) {
        clickOnAndGoBack(R.string.room_settings_labs_pref_title) { block() }
    }

    fun advancedSettings(block: SettingsAdvancedRobot.() -> Unit) {
        clickOnAndGoBack(R.string.settings_advanced_settings) {
            block(SettingsAdvancedRobot())
        }
    }

    fun helpAndAbout(block: SettingsHelpRobot.() -> Unit) {
        clickOnAndGoBack(R.string.preference_root_help_about) { block(SettingsHelpRobot()) }
    }
}
