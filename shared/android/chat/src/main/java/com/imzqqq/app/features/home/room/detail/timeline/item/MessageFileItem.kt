package com.imzqqq.app.features.home.room.detail.timeline.item

import android.content.Context
import android.graphics.Paint
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import kotlin.math.max
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.features.home.room.detail.timeline.helper.ContentDownloadStateTrackerBinder
import com.imzqqq.app.features.home.room.detail.timeline.helper.ContentUploadStateTrackerBinder
import com.imzqqq.app.features.themes.BubbleThemeUtils
import kotlin.math.ceil

@EpoxyModelClass(layout = R.layout.item_timeline_event_base)
abstract class MessageFileItem : AbsMessageItem<MessageFileItem.Holder>() {

    @EpoxyAttribute
    var filename: CharSequence = ""

    @EpoxyAttribute
    var mxcUrl: String = ""

    @EpoxyAttribute
    @DrawableRes
    var iconRes: Int = 0

//    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
//    var clickListener: ClickListener? = null

    @EpoxyAttribute
    var izLocalFile = false

    @EpoxyAttribute
    var izDownloaded = false

    @EpoxyAttribute
    lateinit var contentUploadStateTrackerBinder: ContentUploadStateTrackerBinder

    @EpoxyAttribute
    lateinit var contentDownloadStateTrackerBinder: ContentDownloadStateTrackerBinder

    override fun bind(holder: Holder) {
        super.bind(holder)
        renderSendState(holder.fileLayout, holder.filenameView)
        if (!attributes.informationData.sendState.hasFailed()) {
            contentUploadStateTrackerBinder.bind(attributes.informationData.eventId, izLocalFile, holder.progressLayout)
        } else {
            holder.fileImageView.setImageResource(R.drawable.ic_cross)
            holder.progressLayout.isVisible = false
        }
        holder.filenameView.text = filename
        if (attributes.informationData.sendState.isSending()) {
            holder.fileImageView.setImageResource(iconRes)
        } else {
            if (izDownloaded) {
                holder.fileImageView.setImageResource(iconRes)
                holder.fileDownloadProgress.progress = 100
            } else {
                contentDownloadStateTrackerBinder.bind(mxcUrl, holder)
                holder.fileImageView.setImageResource(R.drawable.ic_download)
                holder.fileDownloadProgress.progress = 0
            }
        }
//        holder.view.setOnClickListener(clickListener)

        holder.filenameView.onClick(attributes.itemClickListener)
        holder.filenameView.setOnLongClickListener(attributes.itemLongClickListener)
        holder.fileImageWrapper.onClick(attributes.itemClickListener)
        holder.fileImageWrapper.setOnLongClickListener(attributes.itemLongClickListener)
        holder.filenameView.paintFlags = (holder.filenameView.paintFlags or Paint.UNDERLINE_TEXT_FLAG)
    }

    override fun unbind(holder: Holder) {
        super.unbind(holder)
        contentUploadStateTrackerBinder.unbind(attributes.informationData.eventId)
        contentDownloadStateTrackerBinder.unbind(mxcUrl)
    }

    override fun getViewType() = STUB_ID

    override fun messageBubbleAllowed(context: Context): Boolean {
        return true
    }

    override fun getViewStubMinimumWidth(holder: Holder, contentInBubble: Boolean, showInformation: Boolean): Int {
        val superVal = super.getViewStubMinimumWidth(holder, contentInBubble, showInformation)

        // Guess text width for name and time
        val density = holder.filenameView.resources.displayMetrics.density
        // On first call, holder.fileImageView.width is not initialized yet
        val imageWidth = holder.fileImageView.resources.getDimensionPixelSize(R.dimen.chat_avatar_size)
        val minimumWidthWithText = ceil(BubbleThemeUtils.guessTextWidth(holder.filenameView, filename)).toInt() + imageWidth + 32*density.toInt()
        val absoluteMinimumWidth = imageWidth*3
        return max(max(absoluteMinimumWidth, minimumWidthWithText), superVal)
    }

    class Holder : AbsMessageItem.Holder(STUB_ID) {
        val progressLayout by bind<ViewGroup>(R.id.messageFileUploadProgressLayout)
        val fileLayout by bind<ViewGroup>(R.id.messageFileLayout)
        val fileImageView by bind<ImageView>(R.id.messageFileIconView)
        val fileImageWrapper by bind<ViewGroup>(R.id.messageFileImageView)
        val fileDownloadProgress by bind<ProgressBar>(R.id.messageFileProgressbar)
        val filenameView by bind<TextView>(R.id.messageFilenameView)
    }

    companion object {
        private const val STUB_ID = R.id.messageContentFileStub
    }
}
