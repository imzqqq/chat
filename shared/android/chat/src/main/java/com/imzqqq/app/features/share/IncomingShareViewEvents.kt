package com.imzqqq.app.features.share

import com.imzqqq.app.core.platform.VectorViewEvents
import org.matrix.android.sdk.api.session.content.ContentAttachmentData
import org.matrix.android.sdk.api.session.room.model.RoomSummary

sealed class IncomingShareViewEvents : VectorViewEvents {
    data class ShareToRoom(val roomSummary: RoomSummary,
                           val sharedData: SharedData,
                           val showAlert: Boolean) : IncomingShareViewEvents()

    data class EditMediaBeforeSending(val contentAttachmentData: List<ContentAttachmentData>) : IncomingShareViewEvents()
    data class MultipleRoomsShareDone(val roomId: String) : IncomingShareViewEvents()
}
