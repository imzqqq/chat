/* Copyright 2019 Flow Contributors
 *
 * This file is a part of Flow.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Flow is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Flow; if not,
 * see <http://www.gnu.org/licenses>. */

package com.keylesspalace.flow.entity

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
