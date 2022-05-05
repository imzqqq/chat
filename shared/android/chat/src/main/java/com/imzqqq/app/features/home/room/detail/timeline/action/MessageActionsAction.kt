package com.imzqqq.app.features.home.room.detail.timeline.action

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class MessageActionsAction : VectorViewModelAction {
    object ToggleReportMenu : MessageActionsAction()
}
