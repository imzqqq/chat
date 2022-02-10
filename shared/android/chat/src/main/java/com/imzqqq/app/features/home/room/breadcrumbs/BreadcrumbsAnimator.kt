package com.imzqqq.app.features.home.room.breadcrumbs

import androidx.recyclerview.widget.DefaultItemAnimator

private const val ANIM_DURATION_IN_MILLIS = 200L

class BreadcrumbsAnimator : DefaultItemAnimator() {

    init {
        addDuration = ANIM_DURATION_IN_MILLIS
        removeDuration = ANIM_DURATION_IN_MILLIS
        moveDuration = ANIM_DURATION_IN_MILLIS
        changeDuration = ANIM_DURATION_IN_MILLIS
    }
}
