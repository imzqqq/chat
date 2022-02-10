package com.imzqqq.app.features.html

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.imzqqq.app.R
import com.imzqqq.app.core.resources.ColorProvider

class SpoilerSpan(private val colorProvider: ColorProvider) : ClickableSpan() {

    override fun onClick(widget: View) {
        isHidden = !isHidden
        widget.invalidate()
    }

    private var isHidden = true

    override fun updateDrawState(tp: TextPaint) {
        if (isHidden) {
            tp.bgColor = colorProvider.getColorFromAttribute(R.attr.vctr_spoiler_background_color)
            tp.color = Color.TRANSPARENT
        } else {
            tp.bgColor = colorProvider.getColorFromAttribute(R.attr.vctr_markdown_block_background_color)
            tp.color = colorProvider.getColorFromAttribute(R.attr.vctr_content_primary)
        }
    }
}
