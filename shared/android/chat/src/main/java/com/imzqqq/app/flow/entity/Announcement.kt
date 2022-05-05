package com.imzqqq.app.flow.entity

import android.text.Spanned
import com.google.gson.annotations.SerializedName
import java.util.Date

data class Announcement(
        val id: String,
        val content: Spanned,
        @SerializedName("starts_at") val startsAt: Date?,
        @SerializedName("ends_at") val endsAt: Date?,
        @SerializedName("all_day") val allDay: Boolean,
        @SerializedName("published_at") val publishedAt: Date,
        @SerializedName("updated_at") val updatedAt: Date,
        val read: Boolean,
        val mentions: List<Status.Mention>,
        val statuses: List<Status>,
        val tags: List<HashTag>,
        val emojis: List<Emoji>,
        val reactions: List<Reaction>
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val announcement = other as Announcement?
        return id == announcement?.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    data class Reaction(
        val name: String,
        var count: Int,
        var me: Boolean,
        val url: String?,
        @SerializedName("static_url") val staticUrl: String?
    )
}
