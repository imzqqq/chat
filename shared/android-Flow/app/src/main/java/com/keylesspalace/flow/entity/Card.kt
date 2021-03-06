/* Copyright 2017 Andrew Dawson
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

import android.text.Spanned
import com.google.gson.annotations.SerializedName

data class Card(
    val url: String,
    val title: Spanned,
    val description: Spanned,
    @SerializedName("author_name") val authorName: String,
    val image: String,
    val type: String,
    val width: Int,
    val height: Int,
    val blurhash: String?,
    val embed_url: String?
) {

    override fun hashCode(): Int {
        return url.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Card) {
            return false
        }
        val account = other as Card?
        return account?.url == this.url
    }

    companion object {
        const val TYPE_PHOTO = "photo"
    }
}
