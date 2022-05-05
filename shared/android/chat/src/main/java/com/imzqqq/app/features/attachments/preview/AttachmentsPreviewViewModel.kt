package com.imzqqq.app.features.attachments.preview

import com.imzqqq.app.core.extensions.exhaustive
import com.imzqqq.app.core.platform.VectorViewModel

class AttachmentsPreviewViewModel(initialState: AttachmentsPreviewViewState) :
    VectorViewModel<AttachmentsPreviewViewState, AttachmentsPreviewAction, AttachmentsPreviewViewEvents>(initialState) {

    override fun handle(action: AttachmentsPreviewAction) {
        when (action) {
            is AttachmentsPreviewAction.SetCurrentAttachment          -> handleSetCurrentAttachment(action)
            is AttachmentsPreviewAction.UpdatePathOfCurrentAttachment -> handleUpdatePathOfCurrentAttachment(action)
            AttachmentsPreviewAction.RemoveCurrentAttachment          -> handleRemoveCurrentAttachment()
        }.exhaustive
    }

    private fun handleRemoveCurrentAttachment() = withState {
        val currentAttachment = it.attachments.getOrNull(it.currentAttachmentIndex) ?: return@withState
        val attachments = it.attachments.minusElement(currentAttachment)
        val newAttachmentIndex = it.currentAttachmentIndex.coerceAtMost(attachments.size - 1)
        setState {
            copy(attachments = attachments, currentAttachmentIndex = newAttachmentIndex)
        }
    }

    private fun handleUpdatePathOfCurrentAttachment(action: AttachmentsPreviewAction.UpdatePathOfCurrentAttachment) = withState {
        val attachments = it.attachments.mapIndexed { index, contentAttachmentData ->
            if (index == it.currentAttachmentIndex) {
                contentAttachmentData.copy(queryUri = action.newUri)
            } else {
                contentAttachmentData
            }
        }
        setState {
            copy(attachments = attachments)
        }
    }

    private fun handleSetCurrentAttachment(action: AttachmentsPreviewAction.SetCurrentAttachment) = setState {
        copy(currentAttachmentIndex = action.index)
    }
}
