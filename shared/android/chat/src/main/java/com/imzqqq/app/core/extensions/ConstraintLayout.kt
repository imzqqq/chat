package com.imzqqq.app.core.extensions

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

fun ConstraintLayout.updateConstraintSet(block: (ConstraintSet) -> Unit) {
    ConstraintSet().let {
        it.clone(this)
        block.invoke(it)
        it.applyTo(this)
    }
}
