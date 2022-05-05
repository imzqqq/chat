package com.imzqqq.app.features.home.room.list

import android.view.HapticFeedbackConstants
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.amulyakhare.textdrawable.TextDrawable
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.core.extensions.setTextOrHide
import com.imzqqq.app.core.ui.views.PresenceStateImageView
import com.imzqqq.app.core.ui.views.ShieldImageView
import com.imzqqq.app.features.displayname.getBestName
import com.imzqqq.app.features.home.AvatarRenderer
import com.imzqqq.app.features.themes.ThemeUtils
import org.matrix.android.sdk.api.crypto.RoomEncryptionTrustLevel
import org.matrix.android.sdk.api.session.presence.model.UserPresence
import org.matrix.android.sdk.api.util.MatrixItem

@EpoxyModelClass(layout = R.layout.item_room)
abstract class RoomSummaryItem : VectorEpoxyModel<RoomSummaryItem.Holder>() {

    @EpoxyAttribute lateinit var typingMessage: CharSequence
    @EpoxyAttribute lateinit var avatarRenderer: AvatarRenderer
    @EpoxyAttribute lateinit var matrixItem: MatrixItem

    // Used only for diff calculation
    @EpoxyAttribute lateinit var lastEvent: String

    // We use DoNotHash here as Spans are not implementing equals/hashcode
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) lateinit var lastFormattedEvent: CharSequence
    @EpoxyAttribute lateinit var lastEventTime: CharSequence
    @EpoxyAttribute var encryptionTrustLevel: RoomEncryptionTrustLevel? = null
    @EpoxyAttribute var userPresence: UserPresence? = null
    @EpoxyAttribute var showPresence: Boolean = false
    @EpoxyAttribute var izPublic: Boolean = false
    @EpoxyAttribute var unreadNotificationCount: Int = 0
    @EpoxyAttribute var hasUnreadMessage: Boolean = false
    @EpoxyAttribute var markedUnread: Boolean = false
    @EpoxyAttribute var unreadCount: Int? = null
    @EpoxyAttribute var hasDraft: Boolean = false
    @EpoxyAttribute var showHighlighted: Boolean = false
    @EpoxyAttribute var hasFailedSending: Boolean = false
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var itemLongClickListener: View.OnLongClickListener? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var itemClickListener: ClickListener? = null
    @EpoxyAttribute var showSelected: Boolean = false

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.rootView.onClick(itemClickListener)
        holder.rootView.setOnLongClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            itemLongClickListener?.onLongClick(it) ?: false
        }
        holder.titleView.text = matrixItem.getBestName()
        holder.lastEventTimeView.text = lastEventTime
        holder.lastEventView.text = lastFormattedEvent
        holder.unreadCounterBadgeView.render(UnreadCounterBadgeView.State(unreadNotificationCount, showHighlighted, unreadCount ?: 0, markedUnread))
        holder.unreadIndentIndicator.isVisible = hasUnreadMessage
        holder.draftView.isVisible = hasDraft
        avatarRenderer.render(matrixItem, holder.avatarImageView)
        holder.roomAvatarDecorationImageView.render(encryptionTrustLevel)
        holder.roomAvatarPublicDecorationImageView.isVisible = izPublic
        holder.roomAvatarFailSendingImageView.isVisible = hasFailedSending
        renderSelection(holder, showSelected)
        holder.typingView.setTextOrHide(typingMessage)
        holder.lastEventView.isInvisible = holder.typingView.isVisible
        holder.roomAvatarPresenceImageView.render(showPresence, userPresence)
    }

    override fun unbind(holder: Holder) {
        holder.rootView.setOnClickListener(null)
        holder.rootView.setOnLongClickListener(null)
        avatarRenderer.clear(holder.avatarImageView)
        super.unbind(holder)
    }

    private fun renderSelection(holder: Holder, isSelected: Boolean) {
        if (isSelected) {
            holder.avatarCheckedImageView.visibility = View.VISIBLE
            val backgroundColor = ThemeUtils.getColor(holder.view.context, R.attr.colorPrimary)
            val backgroundDrawable = TextDrawable.builder().buildRound("", backgroundColor)
            holder.avatarImageView.setImageDrawable(backgroundDrawable)
        } else {
            holder.avatarCheckedImageView.visibility = View.GONE
            avatarRenderer.render(matrixItem, holder.avatarImageView)
        }
    }

    class Holder : VectorEpoxyHolder() {
        val titleView by bind<TextView>(R.id.roomNameView)
        val unreadCounterBadgeView by bind<UnreadCounterBadgeView>(R.id.roomUnreadCounterBadgeView)
        val unreadIndentIndicator by bind<View>(R.id.roomUnreadIndicator)
        val lastEventView by bind<TextView>(R.id.roomLastEventView)
        val typingView by bind<TextView>(R.id.roomTypingView)
        val draftView by bind<ImageView>(R.id.roomDraftBadge)
        val lastEventTimeView by bind<TextView>(R.id.roomLastEventTimeView)
        val avatarCheckedImageView by bind<ImageView>(R.id.roomAvatarCheckedImageView)
        val avatarImageView by bind<ImageView>(R.id.roomAvatarImageView)
        val roomAvatarDecorationImageView by bind<ShieldImageView>(R.id.roomAvatarDecorationImageView)
        val roomAvatarPublicDecorationImageView by bind<ImageView>(R.id.roomAvatarPublicDecorationImageView)
        val roomAvatarFailSendingImageView by bind<ImageView>(R.id.roomAvatarFailSendingImageView)
        val roomAvatarPresenceImageView by bind<PresenceStateImageView>(R.id.roomAvatarPresenceImageView)
        val rootView by bind<ViewGroup>(R.id.itemRoomLayout)
    }
}
