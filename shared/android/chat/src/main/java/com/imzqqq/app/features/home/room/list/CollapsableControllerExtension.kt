package com.imzqqq.app.features.home.room.list

import com.airbnb.epoxy.EpoxyController
import timber.log.Timber

fun EpoxyController.setCollapsed(collapsed: Boolean) {
    if (this is CollapsableControllerExtension) {
        this.collapsed = collapsed
    } else {
        Timber.w("Try to collapse a controller that do not support collapse state")
    }
}

interface CollapsableControllerExtension {
    var collapsed: Boolean
}
