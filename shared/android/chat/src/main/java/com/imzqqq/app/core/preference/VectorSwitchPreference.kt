package com.imzqqq.app.core.preference

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.preference.PreferenceViewHolder
import androidx.preference.SwitchPreference
import com.imzqqq.app.R
import com.imzqqq.app.features.themes.ThemeUtils

/**
 * Switch preference with title on multiline (only used in XML)
 */
class VectorSwitchPreference : SwitchPreference {

    // Note: @JvmOverload does not work here...
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    init {
        // Set to false to remove the space when there is no icon
        isIconSpaceReserved = true
    }

    var isHighlighted = false
        set(value) {
            field = value
            notifyChanged()
        }

    var currentHighlightAnimator: Animator? = null

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        // display the title in multi-line to avoid ellipsis.
        (holder.findViewById(android.R.id.title) as? TextView)?.isSingleLine = false

        // cancel existing animation (find a way to resume if happens during anim?)
        currentHighlightAnimator?.cancel()

        val itemView = holder.itemView
        if (isHighlighted) {
            val colorFrom = Color.TRANSPARENT
            val colorTo = ThemeUtils.getColor(itemView.context, R.attr.colorControlHighlight)
            currentHighlightAnimator = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo).apply {
                duration = 250 // milliseconds
                addUpdateListener { animator ->
                    itemView.setBackgroundColor(animator.animatedValue as Int)
                }
                doOnEnd {
                    currentHighlightAnimator = ValueAnimator.ofObject(ArgbEvaluator(), colorTo, colorFrom).apply {
                        duration = 250 // milliseconds
                        addUpdateListener { animator ->
                            itemView.setBackgroundColor(animator.animatedValue as Int)
                        }
                        doOnEnd {
                            isHighlighted = false
                        }
                        start()
                    }
                }
                startDelay = 200
                start()
            }
        } else {
            val bgDrawable = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, bgDrawable, true)
            ViewCompat.setBackground(itemView, ContextCompat.getDrawable(context, bgDrawable.resourceId))
        }

        super.onBindViewHolder(holder)
    }
}
