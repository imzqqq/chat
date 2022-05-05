package com.imzqqq.app.features.share

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.matrix.android.sdk.api.session.content.ContentAttachmentData

sealed class SharedData : Parcelable {

    @Parcelize
    data class Text(val text: String) : SharedData()

    @Parcelize
    data class Attachments(val attachmentData: List<ContentAttachmentData>) : SharedData()
}
