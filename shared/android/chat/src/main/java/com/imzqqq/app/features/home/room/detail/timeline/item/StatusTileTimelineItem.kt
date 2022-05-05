package com.imzqqq.app.features.home.room.detail.timeline.item

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.features.home.AvatarRenderer
import com.imzqqq.app.features.home.room.detail.timeline.MessageColorProvider
import com.imzqqq.app.features.home.room.detail.timeline.TimelineEventController

@EpoxyModelClass(layout = R.layout.item_timeline_event_base_state)
abstract class StatusTileTimelineItem : AbsBaseMessageItem<StatusTileTimelineItem.Holder>() {

    override val baseAttributes: AbsBaseMessageItem.Attributes
        get() = attributes

    @EpoxyAttribute
    lateinit var attributes: Attributes

    override fun getViewType() = STUB_ID

    @SuppressLint("SetTextI18n")
    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.endGuideline.updateLayoutParams<RelativeLayout.LayoutParams> {
            this.marginEnd = leftGuideline
        }

        holder.titleView.text = attributes.title
        holder.descriptionView.text = attributes.description
        holder.descriptionView.textAlignment = View.TEXT_ALIGNMENT_CENTER

        val startDrawable = when (attributes.shieldUIState) {
            ShieldUIState.GREEN -> R.drawable.ic_shield_trusted
            ShieldUIState.BLACK -> R.drawable.ic_shield_black
            ShieldUIState.RED   -> R.drawable.ic_shield_warning
        }

        holder.titleView.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(holder.view.context, startDrawable),
                null, null, null
        )

        renderSendState(holder.view, null, holder.failedToSendIndicator)
    }

    class Holder : AbsBaseMessageItem.Holder(STUB_ID) {
        val titleView by bind<AppCompatTextView>(R.id.itemVerificationDoneTitleTextView)
        val descriptionView by bind<AppCompatTextView>(R.id.itemVerificationDoneDetailTextView)
        val endGuideline by bind<View>(R.id.messageEndGuideline)
        val failedToSendIndicator by bind<ImageView>(R.id.messageFailToSendIndicator)
    }

    companion object {
        private const val STUB_ID = R.id.messageVerificationDoneStub
    }

    /**
     * This class holds all the common attributes for timeline items.
     */
    data class Attributes(
            val shieldUIState: ShieldUIState,
            val title: CharSequence,
            val description: CharSequence,
            override val informationData: MessageInformationData,
            override val avatarRenderer: AvatarRenderer,
            override val messageColorProvider: MessageColorProvider,
            override val itemLongClickListener: View.OnLongClickListener? = null,
            override val itemClickListener: ClickListener? = null,
            override val reactionPillCallback: TimelineEventController.ReactionPillCallback? = null,
            override val readReceiptsCallback: TimelineEventController.ReadReceiptsCallback? = null,
            val emojiTypeFace: Typeface? = null
    ) : AbsBaseMessageItem.Attributes

    enum class ShieldUIState {
        BLACK,
        RED,
        GREEN
    }
}
