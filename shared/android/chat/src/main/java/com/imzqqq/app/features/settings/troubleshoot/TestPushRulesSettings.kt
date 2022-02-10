package com.imzqqq.app.features.settings.troubleshoot

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.imzqqq.app.R
import com.imzqqq.app.core.di.ActiveSessionHolder
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.features.notifications.toNotificationAction
import org.matrix.android.sdk.api.pushrules.RuleIds
import org.matrix.android.sdk.api.pushrules.getActions
import javax.inject.Inject

class TestPushRulesSettings @Inject constructor(private val activeSessionHolder: ActiveSessionHolder,
                                                private val stringProvider: StringProvider) :
    TroubleshootTest(R.string.settings_troubleshoot_test_bing_settings_title) {

    private val testedRules =
            listOf(RuleIds.RULE_ID_CONTAIN_DISPLAY_NAME,
                    RuleIds.RULE_ID_CONTAIN_USER_NAME,
                    RuleIds.RULE_ID_ONE_TO_ONE_ROOM,
                    RuleIds.RULE_ID_ALL_OTHER_MESSAGES_ROOMS)

    override fun perform(activityResultLauncher: ActivityResultLauncher<Intent>) {
        val session = activeSessionHolder.getSafeActiveSession() ?: return
        val pushRules = session.getPushRules().getAllRules()
        var oneOrMoreRuleIsOff = false
        var oneOrMoreRuleAreSilent = false
        testedRules.forEach { ruleId ->
            pushRules.find { it.ruleId == ruleId }?.let { rule ->
                val actions = rule.getActions()
                val notifAction = actions.toNotificationAction()
                if (!rule.enabled || !notifAction.shouldNotify) {
                    // off
                    oneOrMoreRuleIsOff = true
                } else if (notifAction.soundName == null) {
                    // silent
                    oneOrMoreRuleAreSilent = true
                } else {
                    // noisy
                }
            }
        }

        if (oneOrMoreRuleIsOff) {
            description = stringProvider.getString(R.string.settings_troubleshoot_test_bing_settings_failed)
            // TODO
//                quickFix = object : TroubleshootQuickFix(R.string.settings_troubleshoot_test_bing_settings_quickfix) {
//                    override fun doFix() {
//                        val activity = fragment.activity
//                        if (activity is VectorSettingsFragmentInteractionListener) {
//                            activity.requestHighlightPreferenceKeyOnResume(VectorPreferences.SETTINGS_NOTIFICATION_ADVANCED_PREFERENCE_KEY)
//                        }
//                        activity?.supportFragmentManager?.popBackStack()
//                    }
//                }
            status = TestStatus.FAILED
        } else {
            description = if (oneOrMoreRuleAreSilent) {
                stringProvider.getString(R.string.settings_troubleshoot_test_bing_settings_success_with_warn)
            } else {
                null
            }
            status = TestStatus.SUCCESS
        }
    }
}
