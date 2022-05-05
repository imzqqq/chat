package com.imzqqq.app.features.attachments.preview

import android.net.Uri
import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class AttachmentsPreviewAction : VectorViewModelAction {
    object RemoveCurrentAttachment : AttachmentsPreviewAction()
    data class SetCurrentAttachment(val index: Int) : AttachmentsPreviewAction()
    data class UpdatePathOfCurrentAttachment(val newUri: Uri) : AttachmentsPreviewAction()
}
