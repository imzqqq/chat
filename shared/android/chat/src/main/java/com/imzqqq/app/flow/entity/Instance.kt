package com.imzqqq.app.flow.entity

import com.google.gson.annotations.SerializedName

data class Instance(
        val uri: String,
        val title: String,
        val description: String,
        val email: String,
        val version: String,
        val urls: Map<String, String>,
        val stats: Map<String, Int>?,
        val thumbnail: String?,
        val languages: List<String>,
        @SerializedName("contact_account") val contactAccount: Account,
        @SerializedName("max_toot_chars") val maxTootChars: Int?,
        @SerializedName("max_bio_chars") val maxBioChars: Int?,
        @SerializedName("poll_limits") val pollLimits: PollLimits?
) {
    override fun hashCode(): Int {
        return uri.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Instance) {
            return false
        }
        val instance = other as Instance?
        return instance?.uri.equals(uri)
    }
}

data class PollLimits(
    @SerializedName("max_options") val maxOptions: Int?,
    @SerializedName("max_option_chars") val maxOptionChars: Int?
)
