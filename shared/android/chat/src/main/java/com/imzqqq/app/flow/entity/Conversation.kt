package com.imzqqq.app.flow.entity

import com.google.gson.annotations.SerializedName

data class Conversation(
        val id: String,
        val accounts: List<Account>,
        @SerializedName("last_status") val lastStatus: Status?, // should never be null, but apparently its possible https://github.com/tuskyapp/Tusky/issues/1038
        val unread: Boolean
)
