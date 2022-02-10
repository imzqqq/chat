package com.imzqqq.app.features.devtools

import com.imzqqq.app.core.platform.VectorViewModelAction
import org.matrix.android.sdk.api.session.events.model.Event

sealed class RoomDevToolAction : VectorViewModelAction {
    object ExploreRoomState : RoomDevToolAction()
    object OnBackPressed : RoomDevToolAction()
    object MenuEdit : RoomDevToolAction()
    object MenuItemSend : RoomDevToolAction()
    data class ShowStateEvent(val event: Event) : RoomDevToolAction()
    data class ShowStateEventType(val stateEventType: String) : RoomDevToolAction()
    data class UpdateContentText(val contentJson: String) : RoomDevToolAction()
    data class SendCustomEvent(val isStateEvent: Boolean) : RoomDevToolAction()
    data class CustomEventTypeChange(val type: String) : RoomDevToolAction()
    data class CustomEventContentChange(val content: String) : RoomDevToolAction()
    data class CustomEventStateKeyChange(val stateKey: String) : RoomDevToolAction()
}
