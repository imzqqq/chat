package com.imzqqq.app.features.devtools

import com.airbnb.epoxy.TypedEpoxyController
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.noResultItem
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.list.genericItem
import me.gujun.android.span.span
import org.json.JSONObject
import javax.inject.Inject

class RoomStateListController @Inject constructor(
        private val stringProvider: StringProvider,
        private val colorProvider: ColorProvider
) : TypedEpoxyController<RoomDevToolViewState>() {

    var interactionListener: DevToolsInteractionListener? = null

    override fun buildModels(data: RoomDevToolViewState?) {
        val host = this
        when (data?.displayMode) {
            RoomDevToolViewState.Mode.StateEventList -> {
                val stateEventsGroups = data.stateEvents.invoke().orEmpty().groupBy { it.getClearType() }

                if (stateEventsGroups.isEmpty()) {
                    noResultItem {
                        id("no state events")
                        text(host.stringProvider.getString(R.string.no_result_placeholder))
                    }
                } else {
                    stateEventsGroups.forEach { entry ->
                        genericItem {
                            id(entry.key)
                            title(entry.key)
                            description(host.stringProvider.getQuantityString(R.plurals.entries, entry.value.size, entry.value.size))
                            itemClickAction {
                                host.interactionListener?.processAction(RoomDevToolAction.ShowStateEventType(entry.key))
                            }
                        }
                    }
                }
            }
            RoomDevToolViewState.Mode.StateEventListByType -> {
                val stateEvents = data.stateEvents.invoke().orEmpty().filter { it.type == data.currentStateType }
                if (stateEvents.isEmpty()) {
                    noResultItem {
                        id("no state events")
                        text(host.stringProvider.getString(R.string.no_result_placeholder))
                    }
                } else {
                    stateEvents.forEach { stateEvent ->
                        val contentJson = JSONObject(stateEvent.content.orEmpty()).toString().let {
                            if (it.length > 140) {
                                it.take(140) + Typography.ellipsis
                            } else {
                                it.take(140)
                            }
                        }
                        genericItem {
                            id(stateEvent.eventId)
                            title(span {
                                +"Type: "
                                span {
                                    textColor = host.colorProvider.getColorFromAttribute(R.attr.vctr_content_secondary)
                                    text = "\"${stateEvent.type}\""
                                    textStyle = "normal"
                                }
                                +"\nState Key: "
                                span {
                                    textColor = host.colorProvider.getColorFromAttribute(R.attr.vctr_content_secondary)
                                    text = stateEvent.stateKey.let { "\"$it\"" }
                                    textStyle = "normal"
                                }
                            })
                            description(contentJson)
                            itemClickAction {
                                host.interactionListener?.processAction(RoomDevToolAction.ShowStateEvent(stateEvent))
                            }
                        }
                    }
                }
            }
            else                                           -> {
                // nop
            }
        }
    }
}
