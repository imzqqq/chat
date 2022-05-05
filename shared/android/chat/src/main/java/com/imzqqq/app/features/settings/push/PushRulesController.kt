package com.imzqqq.app.features.settings.push

import com.airbnb.epoxy.TypedEpoxyController
import com.imzqqq.app.R
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.list.genericFooterItem
import javax.inject.Inject

class PushRulesController @Inject constructor(
        private val stringProvider: StringProvider
) : TypedEpoxyController<PushRulesViewState>() {

    override fun buildModels(data: PushRulesViewState?) {
        val host = this
        data?.let {
            it.rules.forEach {
                pushRuleItem {
                    id(it.ruleId)
                    pushRule(it)
                }
            }
        } ?: run {
            genericFooterItem {
                id("footer")
                text(host.stringProvider.getString(R.string.settings_push_rules_no_rules))
            }
        }
    }
}
