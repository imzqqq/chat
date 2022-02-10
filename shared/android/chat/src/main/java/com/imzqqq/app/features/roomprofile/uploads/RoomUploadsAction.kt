package com.imzqqq.app.features.roomprofile.uploads

import com.imzqqq.app.core.platform.VectorViewModelAction
import org.matrix.android.sdk.api.session.room.uploads.UploadEvent

sealed class RoomUploadsAction : VectorViewModelAction {
    data class Download(val uploadEvent: UploadEvent) : RoomUploadsAction()
    data class Share(val uploadEvent: UploadEvent) : RoomUploadsAction()

    object Retry : RoomUploadsAction()
    object LoadMore : RoomUploadsAction()
}
