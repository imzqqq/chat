package com.imzqqq.app.features.attachments.preview

import com.airbnb.mvrx.MavericksState
import org.matrix.android.sdk.api.session.content.ContentAttachmentData

data class AttachmentsPreviewViewState(
        val attachments: List<ContentAttachmentData>,
        val currentAttachmentIndex: Int = 0,
        val sendImagesWithOriginalSize: Boolean = false
) : MavericksState {

    constructor(args: AttachmentsPreviewArgs) : this(attachments = args.attachments)
}
