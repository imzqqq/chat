package com.imzqqq.app.flow.entity

import com.google.gson.annotations.SerializedName
import java.util.ArrayList
import java.util.Date

data class DeletedStatus(
        var text: String?,
        @SerializedName("in_reply_to_id") var inReplyToId: String?,
        @SerializedName("spoiler_text") val spoilerText: String,
        val visibility: Status.Visibility,
        val sensitive: Boolean,
        @SerializedName("media_attachments") var attachments: ArrayList<Attachment>?,
        val poll: Poll?,
        @SerializedName("created_at") val createdAt: Date
) {
    fun isEmpty(): Boolean {
        return text == null && attachments == null
    }
}
