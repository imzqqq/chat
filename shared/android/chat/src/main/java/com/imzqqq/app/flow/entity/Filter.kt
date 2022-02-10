package com.imzqqq.app.flow.entity

import com.google.gson.annotations.SerializedName

data class Filter(
    val id: String,
    val phrase: String,
    val context: List<String>,
    @SerializedName("expires_at") val expiresAt: String?,
    val irreversible: Boolean,
    @SerializedName("whole_word") val wholeWord: Boolean
) {
    companion object {
        const val HOME = "home"
        const val NOTIFICATIONS = "notifications"
        const val PUBLIC = "public"
        const val THREAD = "thread"
        const val ACCOUNT = "account"
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Filter) {
            return false
        }
        val filter = other as Filter?
        return filter?.id.equals(id)
    }
}
