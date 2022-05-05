package com.imzqqq.app.flow.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class NewStatus(
    val status: String,
    @SerializedName("spoiler_text") val warningText: String,
    @SerializedName("in_reply_to_id") val inReplyToId: String?,
    val visibility: String,
    val sensitive: Boolean,
    @SerializedName("media_ids") val mediaIds: List<String>?,
    @SerializedName("scheduled_at") val scheduledAt: String?,
    val poll: NewPoll?
)

@Parcelize
data class NewPoll(
    val options: List<String>,
    @SerializedName("expires_in") val expiresIn: Int,
    val multiple: Boolean
) : Parcelable
