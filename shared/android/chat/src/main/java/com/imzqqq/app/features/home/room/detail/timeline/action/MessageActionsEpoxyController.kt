package com.imzqqq.app.features.home.room.detail.timeline.action

import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.Success
import com.imzqqq.app.EmojiCompatFontProvider
import com.imzqqq.app.R
import com.imzqqq.app.core.date.DateFormatKind
import com.imzqqq.app.core.date.VectorDateFormatter
import com.imzqqq.app.core.epoxy.bottomSheetDividerItem
import com.imzqqq.app.core.epoxy.bottomsheet.BottomSheetQuickReactionsItem
import com.imzqqq.app.core.epoxy.bottomsheet.bottomSheetActionItem
import com.imzqqq.app.core.epoxy.bottomsheet.bottomSheetMessagePreviewItem
import com.imzqqq.app.core.epoxy.bottomsheet.bottomSheetQuickReactionsItem
import com.imzqqq.app.core.epoxy.bottomsheet.bottomSheetSendStateItem
import com.imzqqq.app.core.error.ErrorFormatter
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.utils.DimensionConverter
import com.imzqqq.app.features.home.AvatarRenderer
import com.imzqqq.app.features.home.room.detail.timeline.TimelineEventController
import com.imzqqq.app.features.home.room.detail.timeline.format.EventDetailsFormatter
import com.imzqqq.app.features.home.room.detail.timeline.image.buildImageContentRendererData
import com.imzqqq.app.features.home.room.detail.timeline.item.E2EDecoration
import com.imzqqq.app.features.home.room.detail.timeline.tools.createLinkMovementMethod
import com.imzqqq.app.features.home.room.detail.timeline.tools.linkify
import com.imzqqq.app.features.media.ImageContentRenderer
import org.matrix.android.sdk.api.extensions.orFalse
import org.matrix.android.sdk.api.failure.Failure
import org.matrix.android.sdk.api.session.room.send.SendState
import javax.inject.Inject

/**
 * Epoxy controller for message action list
 */
class MessageActionsEpoxyController @Inject constructor(
        private val stringProvider: StringProvider,
        private val avatarRenderer: AvatarRenderer,
        private val fontProvider: EmojiCompatFontProvider,
        private val imageContentRenderer: ImageContentRenderer,
        private val dimensionConverter: DimensionConverter,
        private val errorFormatter: ErrorFormatter,
        private val eventDetailsFormatter: EventDetailsFormatter,
        private val dateFormatter: VectorDateFormatter
) : TypedEpoxyController<MessageActionState>() {

    var listener: MessageActionsEpoxyControllerListener? = null

    override fun buildModels(state: MessageActionState) {
        val host = this
        // Message preview
        val date = state.timelineEvent()?.root?.originServerTs
        val formattedDate = dateFormatter.format(date, DateFormatKind.MESSAGE_DETAIL)
        bottomSheetMessagePreviewItem {
            id("preview")
            avatarRenderer(host.avatarRenderer)
            matrixItem(state.informationData.matrixItem)
            movementMethod(createLinkMovementMethod(host.listener))
            imageContentRenderer(host.imageContentRenderer)
            data(state.timelineEvent()?.buildImageContentRendererData(host.dimensionConverter.dpToPx(66)))
            userClicked { host.listener?.didSelectMenuAction(EventSharedAction.OpenUserProfile(state.informationData.senderId)) }
            body(state.messageBody.linkify(host.listener))
            bodyDetails(host.eventDetailsFormatter.format(state.timelineEvent()?.root))
            time(formattedDate)
        }

        // Send state
        val sendState = state.sendState()
        if (sendState?.hasFailed().orFalse()) {
            // Get more details about the error
            val errorMessage = state.timelineEvent()?.root?.sendStateError()
                    ?.let { errorFormatter.toHumanReadable(Failure.ServerError(it, 0)) }
                    ?: stringProvider.getString(R.string.unable_to_send_message)
            bottomSheetSendStateItem {
                id("send_state")
                showProgress(false)
                text(errorMessage)
                drawableStart(R.drawable.ic_warning_badge)
            }
        } else if (sendState?.isSending().orFalse()) {
            bottomSheetSendStateItem {
                id("send_state")
                showProgress(true)
                text(host.stringProvider.getString(R.string.event_status_sending_message))
            }
        } else if (sendState == SendState.SENT) {
            bottomSheetSendStateItem {
                id("send_state")
                showProgress(false)
                drawableStart(R.drawable.ic_message_sent)
                text(host.stringProvider.getString(R.string.event_status_sent_message))
            }
        }

        when (state.informationData.e2eDecoration) {
            E2EDecoration.WARN_IN_CLEAR        -> {
                bottomSheetSendStateItem {
                    id("e2e_clear")
                    showProgress(false)
                    text(host.stringProvider.getString(R.string.unencrypted))
                    drawableStart(R.drawable.ic_shield_warning_small)
                }
            }
            E2EDecoration.WARN_SENT_BY_UNVERIFIED,
            E2EDecoration.WARN_SENT_BY_UNKNOWN -> {
                bottomSheetSendStateItem {
                    id("e2e_unverified")
                    showProgress(false)
                    text(host.stringProvider.getString(R.string.encrypted_unverified))
                    drawableStart(R.drawable.ic_shield_warning_small)
                }
            }
            else                               -> {
                // nothing
            }
        }

        // Quick reactions
        if (state.canReact() && state.quickStates is Success) {
            // Separator
            bottomSheetDividerItem {
                id("reaction_separator")
            }

            bottomSheetQuickReactionsItem {
                id("quick_reaction")
                fontProvider(host.fontProvider)
                texts(state.quickStates()?.map { it.reaction }.orEmpty())
                selecteds(state.quickStates.invoke().map { it.isSelected })
                listener(object : BottomSheetQuickReactionsItem.Listener {
                    override fun didSelect(emoji: String, selected: Boolean) {
                        host.listener?.didSelectMenuAction(EventSharedAction.QuickReact(state.eventId, emoji, selected))
                    }
                })
            }
        }

        if (state.actions.isNotEmpty()) {
            // Separator
            bottomSheetDividerItem {
                id("actions_separator")
            }
        }

        // Action
        state.actions.forEachIndexed { index, action ->
            if (action is EventSharedAction.Separator) {
                bottomSheetDividerItem {
                    id("separator_$index")
                }
            } else {
                bottomSheetActionItem {
                    id("action_$index")
                    iconRes(action.iconResId)
                    textRes(action.titleRes)
                    showExpand(action is EventSharedAction.ReportContent)
                    expanded(state.expendedReportContentMenu)
                    listener { host.listener?.didSelectMenuAction(action) }
                    destructive(action.destructive)
                }

                if (action is EventSharedAction.ReportContent && state.expendedReportContentMenu) {
                    // Special case for report content menu: add the submenu
                    listOf(
                            EventSharedAction.ReportContentSpam(action.eventId, action.senderId),
                            EventSharedAction.ReportContentInappropriate(action.eventId, action.senderId),
                            EventSharedAction.ReportContentCustom(action.eventId, action.senderId)
                    ).forEachIndexed { indexReport, actionReport ->
                        bottomSheetActionItem {
                            id("actionReport_$indexReport")
                            subMenuItem(true)
                            iconRes(actionReport.iconResId)
                            textRes(actionReport.titleRes)
                            listener { host.listener?.didSelectMenuAction(actionReport) }
                        }
                    }
                }
            }
        }
    }

    interface MessageActionsEpoxyControllerListener : TimelineEventController.UrlClickCallback {
        fun didSelectMenuAction(eventAction: EventSharedAction)
    }
}
