package com.imzqqq.app.features.home.room.detail.timeline.reactions

import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Incomplete
import com.airbnb.mvrx.Success
import com.imzqqq.app.EmojiCompatWrapper
import com.imzqqq.app.R
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.list.genericFooterItem
import com.imzqqq.app.core.ui.list.genericLoaderItem
import javax.inject.Inject

/**
 * Epoxy controller for reaction event list
 */
class ViewReactionsEpoxyController @Inject constructor(
        private val stringProvider: StringProvider,
        private val emojiCompatWrapper: EmojiCompatWrapper) :
    TypedEpoxyController<DisplayReactionsViewState>() {

    var listener: Listener? = null

    override fun buildModels(state: DisplayReactionsViewState) {
        val host = this
        when (state.mapReactionKeyToMemberList) {
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
                state.mapReactionKeyToMemberList()?.forEach { reactionInfo ->
                    reactionInfoSimpleItem {
                        id(reactionInfo.eventId)
                        timeStamp(reactionInfo.timestamp)
                        reactionKey(host.emojiCompatWrapper.safeEmojiSpanify(reactionInfo.reactionKey))
                        authorDisplayName(reactionInfo.authorName ?: reactionInfo.authorId)
                        userClicked { host.listener?.didSelectUser(reactionInfo.authorId) }
                    }
                }
            }
        }
    }

    interface Listener {
        fun didSelectUser(userId: String)
    }
}
