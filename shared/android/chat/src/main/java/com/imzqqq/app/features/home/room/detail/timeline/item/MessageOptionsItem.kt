package com.imzqqq.app.features.home.room.detail.timeline.item

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.google.android.material.button.MaterialButton
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.core.extensions.setTextOrHide
import com.imzqqq.app.features.home.room.detail.RoomDetailAction
import com.imzqqq.app.features.home.room.detail.timeline.TimelineEventController
import org.matrix.android.sdk.api.session.room.model.message.MessageOptionsContent

@EpoxyModelClass(layout = R.layout.item_timeline_event_base)
abstract class MessageOptionsItem : AbsMessageItem<MessageOptionsItem.Holder>() {

    @EpoxyAttribute
    var optionsContent: MessageOptionsContent? = null

    @EpoxyAttribute
    var callback: TimelineEventController.Callback? = null

    @EpoxyAttribute
    var informationData: MessageInformationData? = null

    override fun getViewType() = STUB_ID

    override fun bind(holder: Holder) {
        super.bind(holder)

        renderSendState(holder.view, holder.labelText)

        holder.labelText.setTextOrHide(optionsContent?.label)

        holder.buttonContainer.removeAllViews()

        val relatedEventId = informationData?.eventId ?: return
        val options = optionsContent?.options?.takeIf { it.isNotEmpty() } ?: return
        // Now add back the buttons
        options.forEachIndexed { index, option ->
            val materialButton = LayoutInflater.from(holder.view.context).inflate(R.layout.option_buttons, holder.buttonContainer, false)
                    as MaterialButton
            holder.buttonContainer.addView(materialButton, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            materialButton.text = option.label
            materialButton.onClick {
                callback?.onTimelineItemAction(RoomDetailAction.ReplyToOptions(relatedEventId, index, option.value ?: "$index"))
            }
        }
    }

    class Holder : AbsMessageItem.Holder(STUB_ID) {

        val labelText by bind<TextView>(R.id.optionLabelText)

        val buttonContainer by bind<ViewGroup>(R.id.optionsButtonContainer)
    }

    companion object {
        private const val STUB_ID = R.id.messageOptionsStub
    }
}
