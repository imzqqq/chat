package com.imzqqq.app.features.html

import android.content.Context
import android.text.Spannable
import android.text.Spanned
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.ActiveSessionHolder
import com.imzqqq.app.core.glide.GlideApp
import com.imzqqq.app.features.home.AvatarRenderer
import io.noties.markwon.core.spans.LinkSpan
import org.matrix.android.sdk.api.session.permalinks.PermalinkData
import org.matrix.android.sdk.api.session.permalinks.PermalinkParser
import org.matrix.android.sdk.api.session.room.model.RoomSummary
import org.matrix.android.sdk.api.util.MatrixItem
import org.matrix.android.sdk.api.util.toMatrixItem

class PillsPostProcessor @AssistedInject constructor(@Assisted private val roomId: String?,
                                                     private val context: Context,
                                                     private val avatarRenderer: AvatarRenderer,
                                                     private val sessionHolder: ActiveSessionHolder) :
    EventHtmlRenderer.PostProcessor {

    @AssistedFactory
    interface Factory {
        fun create(roomId: String?): PillsPostProcessor
    }

    override fun afterRender(renderedText: Spannable) {
        addPillSpans(renderedText, roomId)
    }

    private fun addPillSpans(renderedText: Spannable, roomId: String?) {
        // We let markdown handle links and then we add PillImageSpan if needed.
        val linkSpans = renderedText.getSpans(0, renderedText.length, LinkSpan::class.java)
        linkSpans.forEach { linkSpan ->
            val pillSpan = linkSpan.createPillSpan(roomId) ?: return@forEach
            val startSpan = renderedText.getSpanStart(linkSpan)
            val endSpan = renderedText.getSpanEnd(linkSpan)
            renderedText.setSpan(pillSpan, startSpan, endSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun LinkSpan.createPillSpan(roomId: String?): PillImageSpan? {
        val permalinkData = PermalinkParser.parse(url)
        val matrixItem = when (permalinkData) {
            is PermalinkData.UserLink -> {
                if (roomId == null) {
                    sessionHolder.getSafeActiveSession()?.getUser(permalinkData.userId)?.toMatrixItem()
                } else {
                    sessionHolder.getSafeActiveSession()?.getRoomMember(permalinkData.userId, roomId)?.toMatrixItem()
                }
            }
            is PermalinkData.RoomLink -> {
                if (permalinkData.eventId == null) {
                    val room: RoomSummary? = sessionHolder.getSafeActiveSession()?.getRoomSummary(permalinkData.roomIdOrAlias)
                    if (permalinkData.isRoomAlias) {
                        MatrixItem.RoomAliasItem(permalinkData.roomIdOrAlias, room?.displayName, room?.avatarUrl)
                    } else {
                        MatrixItem.RoomItem(permalinkData.roomIdOrAlias, room?.displayName, room?.avatarUrl)
                    }
                } else {
                    // Exclude event link (used in reply events, we do not want to pill the "in reply to")
                    null
                }
            }
            is PermalinkData.GroupLink -> {
                val group = sessionHolder.getSafeActiveSession()?.getGroupSummary(permalinkData.groupId)
                MatrixItem.GroupItem(permalinkData.groupId, group?.displayName, group?.avatarUrl)
            }
            else                       -> null
        } ?: return null
        return PillImageSpan(GlideApp.with(context), avatarRenderer, context, matrixItem)
    }
}
