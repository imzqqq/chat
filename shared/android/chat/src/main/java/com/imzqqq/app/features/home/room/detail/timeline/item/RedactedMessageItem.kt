package com.imzqqq.app.features.home.room.detail.timeline.item

import android.content.Context
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.features.themes.BubbleThemeUtils

@EpoxyModelClass(layout = R.layout.item_timeline_event_base)
abstract class RedactedMessageItem : AbsMessageItem<RedactedMessageItem.Holder>() {

    override fun getViewType() = STUB_ID

    override fun shouldShowReactionAtBottom() = false

    class Holder : AbsMessageItem.Holder(STUB_ID)

    companion object {
        private const val STUB_ID = R.id.messageContentRedactedStub
    }

    override fun messageBubbleAllowed(context: Context): Boolean {
        return BubbleThemeUtils.getBubbleStyle(context) == BubbleThemeUtils.BUBBLE_STYLE_BOTH
    }
}
