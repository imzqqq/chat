package com.imzqqq.app.core.animations

import android.content.Context
import android.util.AttributeSet
import androidx.transition.ChangeBounds
import androidx.transition.ChangeTransform
import androidx.transition.Fade
import androidx.transition.TransitionSet

class VectorFullTransitionSet : TransitionSet {

    constructor() {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        ordering = ORDERING_TOGETHER
        addTransition(Fade(Fade.OUT))
                .addTransition(ChangeBounds())
                .addTransition(ChangeTransform())
                .addTransition(Fade(Fade.IN))
    }
}
