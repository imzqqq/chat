package com.imzqqq.app.core.ui.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import com.imzqqq.app.R
import com.imzqqq.app.features.home.room.detail.timeline.item.SendStateDecoration
import com.imzqqq.app.features.themes.ThemeUtils

class SendStateImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    init {
        if (isInEditMode) {
            render(SendStateDecoration.SENT)
        }
    }

    fun render(sendState: SendStateDecoration) {
        isVisible = when (sendState) {
            SendStateDecoration.SENDING_NON_MEDIA -> {
                setImageResource(R.drawable.ic_sending_message)
                imageTintList = ColorStateList.valueOf(ThemeUtils.getColor(context, R.attr.vctr_content_tertiary))
                contentDescription = context.getString(R.string.event_status_a11y_sending)
                true
            }
            SendStateDecoration.SENT              -> {
                setImageResource(R.drawable.ic_message_sent)
                imageTintList = ColorStateList.valueOf(ThemeUtils.getColor(context, R.attr.vctr_content_tertiary))
                contentDescription = context.getString(R.string.event_status_a11y_sent)
                true
            }
            SendStateDecoration.FAILED            -> {
                setImageResource(R.drawable.ic_sending_message_failed)
                imageTintList = null
                contentDescription = context.getString(R.string.event_status_a11y_failed)
                true
            }
            SendStateDecoration.SENDING_MEDIA,
            SendStateDecoration.NONE              -> {
                false
            }
        }
    }
}
