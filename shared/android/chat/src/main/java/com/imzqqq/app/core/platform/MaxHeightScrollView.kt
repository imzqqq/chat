package com.imzqqq.app.core.platform

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import androidx.core.widget.NestedScrollView
import com.imzqqq.app.R

private const val DEFAULT_MAX_HEIGHT = 200

class MaxHeightScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    NestedScrollView(context, attrs, defStyle) {

    var maxHeight: Int = 0
        set(value) {
            field = value
            requestLayout()
        }

    init {
        if (attrs != null) {
            context.withStyledAttributes(attrs, R.styleable.MaxHeightScrollView) {
                maxHeight = getDimensionPixelSize(R.styleable.MaxHeightScrollView_maxHeight, DEFAULT_MAX_HEIGHT)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }
}
