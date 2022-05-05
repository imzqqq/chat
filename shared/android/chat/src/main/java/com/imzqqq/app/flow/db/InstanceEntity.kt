package com.imzqqq.app.flow.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.imzqqq.app.flow.entity.Emoji

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
