package com.imzqqq.app.features.devtools

import com.airbnb.epoxy.TypedEpoxyController
import com.imzqqq.app.R
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.list.genericFooterItem
import com.imzqqq.app.features.form.formEditTextItem
import com.imzqqq.app.features.form.formMultiLineEditTextItem
import javax.inject.Inject

class RoomDevToolSendFormController @Inject constructor(
        private val stringProvider: StringProvider
) : TypedEpoxyController<RoomDevToolViewState>() {

    var interactionListener: DevToolsInteractionListener? = null

    override fun buildModels(data: RoomDevToolViewState?) {
        val sendEventForm = (data?.displayMode as? RoomDevToolViewState.Mode.SendEventForm) ?: return
        val host = this

        genericFooterItem {
            id("topSpace")
            text("")
        }
        formEditTextItem {
            id("event_type")
            enabled(true)
            value(data.sendEventDraft?.type)
            hint(host.stringProvider.getString(R.string.dev_tools_form_hint_type))
            onTextChange { text ->
                host.interactionListener?.processAction(RoomDevToolAction.CustomEventTypeChange(text))
            }
        }

        if (sendEventForm.isState) {
            formEditTextItem {
                id("state_key")
                enabled(true)
                value(data.sendEventDraft?.stateKey)
                hint(host.stringProvider.getString(R.string.dev_tools_form_hint_state_key))
                onTextChange { text ->
                    host.interactionListener?.processAction(RoomDevToolAction.CustomEventStateKeyChange(text))
                }
            }
        }

        formMultiLineEditTextItem {
            id("event_content")
            enabled(true)
            value(data.sendEventDraft?.content)
            hint(host.stringProvider.getString(R.string.dev_tools_form_hint_event_content))
            onTextChange { text ->
                host.interactionListener?.processAction(RoomDevToolAction.CustomEventContentChange(text))
            }
        }
    }
}
