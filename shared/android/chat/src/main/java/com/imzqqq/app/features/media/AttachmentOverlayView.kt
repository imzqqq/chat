package com.imzqqq.app.features.media

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.imzqqq.app.R
import com.imzqqq.app.databinding.MergeImageAttachmentOverlayBinding
import im.vector.lib.attachmentviewer.AttachmentEventListener
import im.vector.lib.attachmentviewer.AttachmentEvents

class AttachmentOverlayView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), AttachmentEventListener {

    var onShareCallback: (() -> Unit)? = null
    var onBack: (() -> Unit)? = null
    var onPlayPause: ((play: Boolean) -> Unit)? = null
    var videoSeekTo: ((progress: Int) -> Unit)? = null

    val views: MergeImageAttachmentOverlayBinding

    var isPlaying = false

    var suspendSeekBarUpdate = false

    init {
        inflate(context, R.layout.merge_image_attachment_overlay, this)
        views = MergeImageAttachmentOverlayBinding.bind(this)
        setBackgroundColor(Color.TRANSPARENT)
        views.overlayBackButton.setOnClickListener {
            onBack?.invoke()
        }
        views.overlayShareButton.setOnClickListener {
            onShareCallback?.invoke()
        }
        views.overlayPlayPauseButton.setOnClickListener {
            onPlayPause?.invoke(!isPlaying)
        }

        views.overlaySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    videoSeekTo?.invoke(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                suspendSeekBarUpdate = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                suspendSeekBarUpdate = false
            }
        })
    }

    fun updateWith(counter: String, senderInfo: String) {
        views.overlayCounterText.text = counter
        views.overlayInfoText.text = senderInfo
    }

    override fun onEvent(event: AttachmentEvents) {
        when (event) {
            is AttachmentEvents.VideoEvent -> {
                views.overlayPlayPauseButton.setImageResource(if (!event.isPlaying) R.drawable.ic_play_arrow else R.drawable.ic_pause)
                if (!suspendSeekBarUpdate) {
                    val safeDuration = (if (event.duration == 0) 100 else event.duration).toFloat()
                    val percent = ((event.progress / safeDuration) * 100f).toInt().coerceAtMost(100)
                    isPlaying = event.isPlaying
                    views.overlaySeekBar.progress = percent
                }
            }
        }
    }
}
