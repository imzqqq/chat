package com.imzqqq.app.features.settings.push

import android.annotation.SuppressLint
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.features.notifications.toNotificationAction
import com.imzqqq.app.features.themes.ThemeUtils
import org.matrix.android.sdk.api.pushrules.getActions
import org.matrix.android.sdk.api.pushrules.rest.PushRule

@EpoxyModelClass(layout = R.layout.item_pushrule_raw)
abstract class PushRuleItem : EpoxyModelWithHolder<PushRuleItem.Holder>() {

    @EpoxyAttribute
    lateinit var pushRule: PushRule

    // TODO i18n
    @SuppressLint("SetTextI18n")
    override fun bind(holder: Holder) {
        super.bind(holder)
        val context = holder.view.context
        if (pushRule.enabled) {
            holder.view.setBackgroundColor(Color.TRANSPARENT)
            holder.ruleId.text = pushRule.ruleId
        } else {
            holder.view.setBackgroundColor(ThemeUtils.getColor(context, R.attr.vctr_header_background))
            holder.ruleId.text = "[Disabled] ${pushRule.ruleId}"
        }
        val actions = pushRule.getActions()
        if (actions.isEmpty()) {
            holder.actionIcon.isInvisible = true
        } else {
            holder.actionIcon.isVisible = true
            val notifAction = actions.toNotificationAction()

            if (notifAction.shouldNotify && !notifAction.soundName.isNullOrBlank()) {
                holder.actionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_action_notify_noisy))
                holder.actionIcon.contentDescription = context.getString(R.string.a11y_rule_notify_noisy)
            } else if (notifAction.shouldNotify) {
                holder.actionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_action_notify_silent))
                holder.actionIcon.contentDescription = context.getString(R.string.a11y_rule_notify_silent)
            } else {
                holder.actionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_action_dont_notify))
                holder.actionIcon.contentDescription = context.getString(R.string.a11y_rule_notify_off)
            }

            val description = StringBuffer()
            pushRule.conditions?.forEachIndexed { i, condition ->
                if (i > 0) description.append("\n")
                description.append(condition.asExecutableCondition(pushRule)?.technicalDescription()
                        ?: "UNSUPPORTED")
            }
            if (description.isBlank()) {
                holder.description.text = "No Conditions"
            } else {
                holder.description.text = description
            }
        }
    }

    class Holder : VectorEpoxyHolder() {
        val ruleId by bind<TextView>(R.id.pushRuleId)
        val description by bind<TextView>(R.id.pushRuleDescription)
        val actionIcon by bind<ImageView>(R.id.pushRuleActionIcon)
    }
}
