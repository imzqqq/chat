package com.imzqqq.app.features.home.room.detail.timeline.action

import com.imzqqq.app.core.platform.VectorSharedActionViewModel
import javax.inject.Inject

/**
 * Activity shared view model to handle message actions
 */
class MessageSharedActionViewModel @Inject constructor() : VectorSharedActionViewModel<EventSharedAction>() {
    var pendingAction: EventSharedAction? = null
}
