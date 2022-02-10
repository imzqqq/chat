package com.imzqqq.app.flow.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Emoji(
    val shortcode: String,
    val url: String,
    @SerializedName("static_url") val staticUrl: String,
    @SerializedName("visible_in_picker") val visibleInPicker: Boolean?
) : Parcelable
