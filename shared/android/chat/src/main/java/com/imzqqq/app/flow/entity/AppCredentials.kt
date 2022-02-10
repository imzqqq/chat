package com.imzqqq.app.flow.entity

import com.google.gson.annotations.SerializedName

data class AppCredentials(
    @SerializedName("client_id") val clientId: String,
    @SerializedName("client_secret") val clientSecret: String
)
