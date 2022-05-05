package com.imzqqq.app.features.devtools

import com.airbnb.epoxy.EpoxyController
import com.imzqqq.app.R
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.list.genericButtonItem
import javax.inject.Inject

class RoomDevToolRootController @Inject constructor(
        private val stringProvider: StringProvider
) : EpoxyController() {

    init {
        requestModelBuild()
    }

    var interactionListener: DevToolsInteractionListener? = null

    override fun buildModels() {
        val host = this
        genericButtonItem {
            id("explore")
            text(host.stringProvider.getString(R.string.dev_tools_explore_room_state))
            buttonClickAction {
                host.interactionListener?.processAction(RoomDevToolAction.ExploreRoomState)
            }
        }
        genericButtonItem {
            id("send")
            text(host.stringProvider.getString(R.string.dev_tools_send_custom_event))
            buttonClickAction {
                host.interactionListener?.processAction(RoomDevToolAction.SendCustomEvent(false))
            }
        }
        genericButtonItem {
            id("send_state")
            text(host.stringProvider.getString(R.string.dev_tools_send_state_event))
            buttonClickAction {
                host.interactionListener?.processAction(RoomDevToolAction.SendCustomEvent(true))
            }
        }
    }
}
