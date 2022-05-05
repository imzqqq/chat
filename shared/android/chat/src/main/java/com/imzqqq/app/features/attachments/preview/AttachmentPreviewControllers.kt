package com.imzqqq.app.features.attachments.preview

import com.airbnb.epoxy.TypedEpoxyController
import org.matrix.android.sdk.api.session.content.ContentAttachmentData
import javax.inject.Inject

class AttachmentBigPreviewController @Inject constructor() : TypedEpoxyController<AttachmentsPreviewViewState>() {

    override fun buildModels(data: AttachmentsPreviewViewState) {
        data.attachments.forEach {
            attachmentBigPreviewItem {
                id(it.queryUri.toString())
                attachment(it)
            }
        }
    }
}

class AttachmentMiniaturePreviewController @Inject constructor() : TypedEpoxyController<AttachmentsPreviewViewState>() {

    interface Callback {
        fun onAttachmentClicked(position: Int, contentAttachmentData: ContentAttachmentData)
    }

    var callback: Callback? = null

    override fun buildModels(data: AttachmentsPreviewViewState) {
        val host = this
        data.attachments.forEachIndexed { index, contentAttachmentData ->
            attachmentMiniaturePreviewItem {
                id(contentAttachmentData.queryUri.toString())
                attachment(contentAttachmentData)
                checked(data.currentAttachmentIndex == index)
                clickListener { _ ->
                    host.callback?.onAttachmentClicked(index, contentAttachmentData)
                }
            }
        }
    }
}
