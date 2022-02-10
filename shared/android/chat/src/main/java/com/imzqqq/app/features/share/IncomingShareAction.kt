package com.imzqqq.app.features.share

import com.imzqqq.app.core.platform.VectorViewModelAction
import org.matrix.android.sdk.api.session.room.model.RoomSummary

sealed class IncomingShareAction : VectorViewModelAction {
    data class SelectRoom(val roomSummary: RoomSummary, val enableMultiSelect: Boolean) : IncomingShareAction()
    object ShareToSelectedRooms : IncomingShareAction()
    data class ShareToRoom(val roomSummary: RoomSummary) : IncomingShareAction()
    data class ShareMedia(val keepOriginalSize: Boolean) : IncomingShareAction()
    data class FilterWith(val filter: String) : IncomingShareAction()
    data class UpdateSharedData(val sharedData: SharedData) : IncomingShareAction()
}
