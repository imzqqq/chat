package com.imzqqq.app.features.home.room.detail.timeline.edithistory

import android.text.Spannable
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Incomplete
import com.airbnb.mvrx.Success
import com.imzqqq.app.R
import com.imzqqq.app.core.date.DateFormatKind
import com.imzqqq.app.core.date.VectorDateFormatter
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.list.genericFooterItem
import com.imzqqq.app.core.ui.list.genericHeaderItem
import com.imzqqq.app.core.ui.list.genericItem
import com.imzqqq.app.core.ui.list.genericLoaderItem
import com.imzqqq.app.features.html.EventHtmlRenderer
import me.gujun.android.span.span
import name.fraser.neil.plaintext.diff_match_patch
import org.matrix.android.sdk.api.session.events.model.Event
import org.matrix.android.sdk.api.session.events.model.toModel
import org.matrix.android.sdk.api.session.room.model.message.MessageTextContent
import org.matrix.android.sdk.api.util.ContentUtils.extractUsefulTextFromReply
import org.matrix.android.sdk.internal.session.room.send.TextContent
import java.util.Calendar
import javax.inject.Inject

/**
 * Epoxy controller for edit history list
 */
class ViewEditHistoryEpoxyController @Inject constructor(
        private val stringProvider: StringProvider,
        private val colorProvider: ColorProvider,
        private val eventHtmlRenderer: EventHtmlRenderer,
        private val dateFormatter: VectorDateFormatter
) : TypedEpoxyController<ViewEditHistoryViewState>() {

    override fun buildModels(state: ViewEditHistoryViewState) {
        val host = this
        when (state.editList) {
            is Incomplete -> {
                genericLoaderItem {
                    id("Spinner")
                }
            }
            is Fail       -> {
                genericFooterItem {
                    id("failure")
                    text(host.stringProvider.getString(R.string.unknown_error))
                }
            }
            is Success    -> {
                state.editList()?.let { renderEvents(it, state.isOriginalAReply) }
            }
        }
    }

    private fun renderEvents(sourceEvents: List<Event>, isOriginalReply: Boolean) {
        val host = this
        if (sourceEvents.isEmpty()) {
            genericItem {
                id("footer")
                title(host.stringProvider.getString(R.string.no_message_edits_found))
            }
        } else {
            var lastDate: Calendar? = null
            sourceEvents.forEachIndexed { index, timelineEvent ->

                val evDate = Calendar.getInstance().apply {
                    timeInMillis = timelineEvent.originServerTs
                            ?: System.currentTimeMillis()
                }
                if (lastDate?.get(Calendar.DAY_OF_YEAR) != evDate.get(Calendar.DAY_OF_YEAR)) {
                    // need to display header with day
                    genericHeaderItem {
                        id(evDate.hashCode())
                        text(host.dateFormatter.format(evDate.timeInMillis, DateFormatKind.EDIT_HISTORY_HEADER))
                    }
                }
                lastDate = evDate
                val cContent = getCorrectContent(timelineEvent, isOriginalReply)
                val body = cContent.formattedText?.let { eventHtmlRenderer.render(it) } ?: cContent.text

                val nextEvent = sourceEvents.getOrNull(index + 1)

                var spannedDiff: Spannable? = null
                if (nextEvent != null && cContent.formattedText == null /*No diff for html*/) {
                    // compares the body
                    val nContent = getCorrectContent(nextEvent, isOriginalReply)
                    val nextBody = nContent.formattedText?.let { eventHtmlRenderer.render(it) } ?: nContent.text
                    val dmp = diff_match_patch()
                    val diff = dmp.diff_main(nextBody.toString(), body.toString())
                    dmp.diff_cleanupSemantic(diff)
                    spannedDiff = span {
                        diff.map {
                            when (it.operation) {
                                diff_match_patch.Operation.DELETE -> {
                                    span {
                                        text = it.text.replace("\n", " ")
                                        textColor = colorProvider.getColorFromAttribute(R.attr.colorError)
                                        textDecorationLine = "line-through"
                                    }
                                }
                                diff_match_patch.Operation.INSERT -> {
                                    span {
                                        text = it.text
                                        textColor = colorProvider.getColor(R.color.palette_element_green)
                                    }
                                }
                                else                              -> {
                                    span {
                                        text = it.text
                                    }
                                }
                            }
                        }
                    }
                }
                genericItem {
                    id(timelineEvent.eventId)
                    title(host.dateFormatter.format(timelineEvent.originServerTs, DateFormatKind.EDIT_HISTORY_ROW))
                    description(spannedDiff ?: body)
                }
            }
        }
    }

    private fun getCorrectContent(event: Event, isOriginalReply: Boolean): TextContent {
        val clearContent = event.getClearContent().toModel<MessageTextContent>()
        val newContent = clearContent
                ?.newContent
                ?.toModel<MessageTextContent>()
        if (isOriginalReply) {
            return TextContent(extractUsefulTextFromReply(newContent?.body ?: clearContent?.body ?: ""))
        }
        return TextContent(newContent?.body ?: clearContent?.body ?: "", newContent?.formattedBody ?: clearContent?.formattedBody)
    }
}
