package com.imzqqq.app.features.roomprofile.settings.joinrule

import com.airbnb.epoxy.TypedEpoxyController
import com.imzqqq.app.R
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.list.ItemStyle
import com.imzqqq.app.core.ui.list.genericButtonItem
import com.imzqqq.app.core.ui.list.genericFooterItem
import com.imzqqq.app.features.home.AvatarRenderer
import com.imzqqq.app.features.roomprofile.settings.joinrule.advanced.RoomJoinRuleChooseRestrictedState
import org.matrix.android.sdk.api.session.room.model.RoomJoinRules
import timber.log.Timber
import javax.inject.Inject

class RoomJoinRuleAdvancedController @Inject constructor(
        private val stringProvider: StringProvider,
        private val colorProvider: ColorProvider,
        private val avatarRenderer: AvatarRenderer
) : TypedEpoxyController<RoomJoinRuleChooseRestrictedState>() {

    interface InteractionListener {
        fun didSelectRule(rules: RoomJoinRules)
    }

    var interactionListener: InteractionListener? = null

    override fun buildModels(state: RoomJoinRuleChooseRestrictedState?) {
        state ?: return
        val choices = state.choices ?: return

        val host = this

        genericFooterItem {
            id("header")
            text(host.stringProvider.getString(R.string.room_settings_room_access_title))
            centered(false)
            style(ItemStyle.TITLE)
            textColor(host.colorProvider.getColorFromAttribute(R.attr.vctr_content_primary))
        }

        genericFooterItem {
            id("desc")
            text(host.stringProvider.getString(R.string.decide_who_can_find_and_join))
            centered(false)
        }

        // invite only
        RoomJoinRuleRadioAction(
                roomJoinRule = RoomJoinRules.INVITE,
                description = stringProvider.getString(R.string.room_settings_room_access_private_description),
                title = stringProvider.getString(R.string.room_settings_room_access_private_invite_only_title),
                isSelected = state.currentRoomJoinRules == RoomJoinRules.INVITE
        ).toRadioBottomSheetItem().let {
            it.listener {
                interactionListener?.didSelectRule(RoomJoinRules.INVITE)
//                listener?.didSelectAction(action)
            }
            add(it)
        }

        if (choices.firstOrNull { it.rule == RoomJoinRules.RESTRICTED } != null) {
            val restrictedRule = choices.first { it.rule == RoomJoinRules.RESTRICTED }
            Timber.w("##@@ ${state.updatedAllowList}")
            spaceJoinRuleItem {
                id("restricted")
                avatarRenderer(host.avatarRenderer)
                needUpgrade(restrictedRule.needUpgrade)
                selected(state.currentRoomJoinRules == RoomJoinRules.RESTRICTED)
                restrictedList(state.updatedAllowList)
                listener { host.interactionListener?.didSelectRule(RoomJoinRules.RESTRICTED) }
            }
        }

        // Public
        RoomJoinRuleRadioAction(
                roomJoinRule = RoomJoinRules.PUBLIC,
                description = stringProvider.getString(R.string.room_settings_room_access_public_description),
                title = stringProvider.getString(R.string.room_settings_room_access_public_title),
                isSelected = state.currentRoomJoinRules == RoomJoinRules.PUBLIC
        ).toRadioBottomSheetItem().let {
            it.listener {
                interactionListener?.didSelectRule(RoomJoinRules.PUBLIC)
            }
            add(it)
        }

        genericButtonItem {
            id("save")
            text("")
        }
    }
}
