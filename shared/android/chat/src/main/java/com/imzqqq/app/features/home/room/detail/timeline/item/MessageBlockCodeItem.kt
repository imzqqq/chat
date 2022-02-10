package com.imzqqq.app.features.home.room.detail.timeline.item

import android.content.Context
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.core.extensions.setTextOrHide
import me.saket.bettermovementmethod.BetterLinkMovementMethod

@EpoxyModelClass(layout = R.layout.item_timeline_event_base)
abstract class MessageBlockCodeItem : AbsMessageItem<MessageBlockCodeItem.Holder>() {

    @EpoxyAttribute
    var message: CharSequence? = null

    @EpoxyAttribute
    var editedSpan: CharSequence? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.messageView.text = message
        renderSendState(holder.messageView, holder.messageView)
        holder.messageView.onClick(attributes.itemClickListener)
        holder.messageView.setOnLongClickListener(attributes.itemLongClickListener)
        holder.editedView.movementMethod = BetterLinkMovementMethod.getInstance()
        holder.editedView.setTextOrHide(editedSpan)
    }

    override fun getViewType() = STUB_ID

    class Holder : AbsMessageItem.Holder(STUB_ID) {
        val messageView by bind<TextView>(R.id.codeBlockTextView)
        val editedView by bind<TextView>(R.id.codeBlockEditedView)
    }

    companion object {
        private const val STUB_ID = R.id.messageContentCodeBlockStub
    }

    override fun messageBubbleAllowed(context: Context): Boolean {
        return true
    }
}
