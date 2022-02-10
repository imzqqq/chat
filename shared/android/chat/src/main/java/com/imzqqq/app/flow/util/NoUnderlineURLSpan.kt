package com.imzqqq.app.flow.util

import android.text.TextPaint
import android.text.style.URLSpan
import android.view.View

open class NoUnderlineURLSpan(
    url: String
) : URLSpan(url) {

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }

    override fun onClick(view: View) {
        com.imzqqq.app.flow.util.LinkHelper.openLink(url, view.context)
    }
}
