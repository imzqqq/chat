/* Copyright 2018 Conny Duck
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

package com.keylesspalace.flow.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.keylesspalace.flow.entity.Emoji

@Entity
@TypeConverters(Converters::class)
data class InstanceEntity(
    @field:PrimaryKey var instance: String,
    val emojiList: List<Emoji>?,
    val maximumTootCharacters: Int?,
    val maxPollOptions: Int?,
    val maxPollOptionLength: Int?,
    val version: String?
)
