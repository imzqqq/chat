package com.imzqqq.app.features.settings.notifications

import com.imzqqq.app.R
import com.imzqqq.app.core.preference.VectorPreferenceCategory
import org.matrix.android.sdk.api.pushrules.RuleIds

class VectorSettingsOtherNotificationPreferenceFragment :
    VectorSettingsPushRuleNotificationPreferenceFragment() {

    override var titleRes: Int = R.string.settings_notification_other

    override val preferenceXmlRes = R.xml.vector_settings_notification_other

    override val prefKeyToPushRuleId = mapOf(
                "SETTINGS_PUSH_RULE_INVITED_TO_ROOM_PREFERENCE_KEY" to RuleIds.RULE_ID_INVITE_ME,
                "SETTINGS_PUSH_RULE_CALL_INVITATIONS_PREFERENCE_KEY" to RuleIds.RULE_ID_CALL,
                "SETTINGS_PUSH_RULE_MESSAGES_SENT_BY_BOT_PREFERENCE_KEY" to RuleIds.RULE_ID_SUPPRESS_BOTS_NOTIFICATIONS,
                "SETTINGS_PUSH_RULE_ROOMS_UPGRADED_KEY" to RuleIds.RULE_ID_TOMBSTONE
        )

    override fun bindPref() {
        super.bindPref()
        val category = findPreference<VectorPreferenceCategory>("SETTINGS_OTHER")!!
        category.isIconSpaceReserved = false
    }
}
