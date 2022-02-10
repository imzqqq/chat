package com.imzqqq.app.features.spaces.preview

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.imzqqq.app.R

class SpaceTabView constructor(context: Context,
                               attrs: AttributeSet? = null,
                               defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {}
    constructor(context: Context) : this(context, null, 0) {}

    var tabDepth = 0
        set(value) {
            if (field != value) {
                field = value
                setUpView()
            }
        }

    init {
        setUpView()
    }

    private fun setUpView() {
        // remove children
        removeAllViews()
        for (i in 0 until tabDepth) {
            inflate(context, R.layout.item_space_tab, this)
        }
    }
}
