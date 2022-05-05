package com.imzqqq.app.features.settings.notifications

import com.imzqqq.app.R
import com.imzqqq.app.core.preference.VectorPreferenceCategory
import org.matrix.android.sdk.api.pushrules.RuleIds

class VectorSettingsDefaultNotificationPreferenceFragment :
    VectorSettingsPushRuleNotificationPreferenceFragment() {

    override var titleRes: Int = R.string.settings_notification_default

    override val preferenceXmlRes = R.xml.vector_settings_notification_default

    override val prefKeyToPushRuleId = mapOf(
                "SETTINGS_PUSH_RULE_MESSAGES_IN_ONE_TO_ONE_PREFERENCE_KEY" to RuleIds.RULE_ID_ONE_TO_ONE_ROOM,
                "SETTINGS_PUSH_RULE_MESSAGES_IN_GROUP_CHAT_PREFERENCE_KEY" to RuleIds.RULE_ID_ALL_OTHER_MESSAGES_ROOMS,
                "SETTINGS_PUSH_RULE_MESSAGES_IN_E2E_ONE_ONE_CHAT_PREFERENCE_KEY" to RuleIds.RULE_ID_ONE_TO_ONE_ENCRYPTED_ROOM,
                "SETTINGS_PUSH_RULE_MESSAGES_IN_E2E_GROUP_CHAT_PREFERENCE_KEY" to RuleIds.RULE_ID_ENCRYPTED
        )

    override fun bindPref() {
        super.bindPref()
        val category = findPreference<VectorPreferenceCategory>("SETTINGS_DEFAULT")!!
        category.isIconSpaceReserved = false
    }
}
