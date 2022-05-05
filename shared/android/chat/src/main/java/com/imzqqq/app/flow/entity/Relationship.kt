package com.imzqqq.app.flow.entity

import com.google.gson.annotations.SerializedName

data class Relationship(
    val id: String,
    val following: Boolean,
    @SerializedName("followed_by") val followedBy: Boolean,
    val blocking: Boolean,
    val muting: Boolean,
    @SerializedName("muting_notifications") val mutingNotifications: Boolean,
    val requested: Boolean,
    @SerializedName("showing_reblogs") val showingReblogs: Boolean,
    val subscribing: Boolean? = null, // Pleroma extension
    @SerializedName("domain_blocking") val blockingDomain: Boolean,
    val note: String?, // nullable for backward compatibility / feature detection
    val notifying: Boolean? // since 3.3.0rc
)
