package com.imzqqq.app.flow.entity

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
