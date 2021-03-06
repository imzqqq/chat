/* Copyright 2019 Flow contributors
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
