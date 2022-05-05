package com.imzqqq.app.features.home.room.detail.timeline.item

import android.content.Context
import android.text.format.DateUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.visualizer.amplitude.AudioRecordView
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.features.home.room.detail.timeline.helper.ContentDownloadStateTrackerBinder
import com.imzqqq.app.features.home.room.detail.timeline.helper.ContentUploadStateTrackerBinder
import com.imzqqq.app.features.home.room.detail.timeline.helper.VoiceMessagePlaybackTracker
import com.imzqqq.app.features.themes.BubbleThemeUtils

@EpoxyModelClass(layout = R.layout.item_timeline_event_base)
abstract class MessageVoiceItem : AbsMessageItem<MessageVoiceItem.Holder>() {

    init {
        ignoreSendStatusVisibility = true
    }

    @EpoxyAttribute
    var mxcUrl: String = ""

    @EpoxyAttribute
    var duration: Int = 0

    @EpoxyAttribute
    var waveform: List<Int> = emptyList()

    @EpoxyAttribute
    var izLocalFile = false

    @EpoxyAttribute
    var izDownloaded = false

    @EpoxyAttribute
    var filename: CharSequence? = null

    @EpoxyAttribute
    lateinit var contentUploadStateTrackerBinder: ContentUploadStateTrackerBinder

    @EpoxyAttribute
    lateinit var contentDownloadStateTrackerBinder: ContentDownloadStateTrackerBinder

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var playbackControlButtonClickListener: ClickListener? = null

    @EpoxyAttribute
    lateinit var voiceMessagePlaybackTracker: VoiceMessagePlaybackTracker

    override fun bind(holder: Holder) {
        super.bind(holder)
        renderSendState(holder.voiceLayout, null)
        if (!attributes.informationData.sendState.hasFailed()) {
            contentUploadStateTrackerBinder.bind(attributes.informationData.eventId, izLocalFile, holder.progressLayout)
        } else {
            holder.voicePlaybackControlButton.setImageResource(R.drawable.ic_cross)
            holder.voicePlaybackControlButton.contentDescription = holder.view.context.getString(R.string.error_voice_message_unable_to_play)
            holder.progressLayout.isVisible = false
        }

        holder.voicePlaybackWaveform.setOnLongClickListener(attributes.itemLongClickListener)

        holder.voicePlaybackWaveform.post {
            holder.voicePlaybackWaveform.recreate()
            waveform.forEach { amplitude ->
                holder.voicePlaybackWaveform.update(amplitude)
            }

            // SC: fallback audio name if no waveform (we also use this for audio messages!)
            if (waveform.isEmpty() && !filename.isNullOrBlank()) {
                holder.voicePlaybackWaveform.isInvisible = true
                holder.voiceMessageName.isVisible = true
                holder.voiceMessageName.text = filename
            } else {
                holder.voicePlaybackWaveform.isVisible = true
                holder.voiceMessageName.isGone = true
            }
        }

        holder.voicePlaybackControlButton.setOnClickListener { playbackControlButtonClickListener?.invoke(it) }

        voiceMessagePlaybackTracker.track(attributes.informationData.eventId, object : VoiceMessagePlaybackTracker.Listener {
            override fun onUpdate(state: VoiceMessagePlaybackTracker.Listener.State) {
                when (state) {
                    is VoiceMessagePlaybackTracker.Listener.State.Idle    -> renderIdleState(holder)
                    is VoiceMessagePlaybackTracker.Listener.State.Playing -> renderPlayingState(holder, state)
                    is VoiceMessagePlaybackTracker.Listener.State.Paused  -> renderPausedState(holder, state)
                }
            }
        })
    }

    private fun renderIdleState(holder: Holder) {
        holder.voicePlaybackControlButton.setImageResource(R.drawable.ic_play_pause_play)
        holder.voicePlaybackControlButton.contentDescription = holder.view.context.getString(R.string.a11y_play_voice_message)
        holder.voicePlaybackTime.text = formatPlaybackTime(duration)
    }

    private fun renderPlayingState(holder: Holder, state: VoiceMessagePlaybackTracker.Listener.State.Playing) {
        holder.voicePlaybackControlButton.setImageResource(R.drawable.ic_play_pause_pause)
        holder.voicePlaybackControlButton.contentDescription = holder.view.context.getString(R.string.a11y_pause_voice_message)
        holder.voicePlaybackTime.text = formatPlaybackTime(state.playbackTime)
    }

    private fun renderPausedState(holder: Holder, state: VoiceMessagePlaybackTracker.Listener.State.Paused) {
        holder.voicePlaybackControlButton.setImageResource(R.drawable.ic_play_pause_play)
        holder.voicePlaybackControlButton.contentDescription = holder.view.context.getString(R.string.a11y_play_voice_message)
        holder.voicePlaybackTime.text = formatPlaybackTime(state.playbackTime)
    }

    private fun formatPlaybackTime(time: Int) = DateUtils.formatElapsedTime((time / 1000).toLong())

    override fun unbind(holder: Holder) {
        super.unbind(holder)
        contentUploadStateTrackerBinder.unbind(attributes.informationData.eventId)
        contentDownloadStateTrackerBinder.unbind(mxcUrl)
        voiceMessagePlaybackTracker.unTrack(attributes.informationData.eventId)
    }

    override fun getViewType() = STUB_ID

    class Holder : AbsMessageItem.Holder(STUB_ID) {
        val voiceLayout by bind<ViewGroup>(R.id.voiceLayout)
        val voicePlaybackControlButton by bind<ImageButton>(R.id.voicePlaybackControlButton)
        val voicePlaybackTime by bind<TextView>(R.id.voicePlaybackTime)
        val voicePlaybackWaveform by bind<AudioRecordView>(R.id.voicePlaybackWaveform)
        val progressLayout by bind<ViewGroup>(R.id.messageFileUploadProgressLayout)
        val voicePlaybackLayout by bind<ConstraintLayout>(R.id.voicePlaybackLayout)
        val voiceMessageName by bind<TextView>(R.id.voiceMessageName)
    }

    companion object {
        private const val STUB_ID = R.id.messageContentVoiceStub
    }

    override fun messageBubbleAllowed(context: Context): Boolean {
        return true
    }

    override fun setBubbleLayout(holder: Holder, bubbleStyle: String, bubbleStyleSetting: String, reverseBubble: Boolean) {
        super.setBubbleLayout(holder, bubbleStyle, bubbleStyleSetting, reverseBubble)

        if (BubbleThemeUtils.drawsActualBubbles(bubbleStyle)) {
            (holder.voiceLayout.layoutParams as ViewGroup.MarginLayoutParams).marginEnd = 0
        } else {
            (holder.voiceLayout.layoutParams as ViewGroup.MarginLayoutParams).marginEnd = holder.voiceLayout.resources.getDimensionPixelSize(R.dimen.no_bubble_margin_end)
        }
    }
}
