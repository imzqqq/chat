package com.imzqqq.app.core.ui.views

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView

class NonScrollingTextView : MaterialTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun scrollTo(x: Int, y: Int) {
        // NOOP
    }
}
