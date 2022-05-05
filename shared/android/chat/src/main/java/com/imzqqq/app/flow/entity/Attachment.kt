package com.imzqqq.app.flow.entity

import android.os.Parcelable
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Attachment(
        val id: String,
        val url: String,
        @SerializedName("preview_url") val previewUrl: String?, // can be null for e.g. audio attachments
        val meta: MetaData?,
        val type: Type,
        val description: String?,
        val blurhash: String?
) : Parcelable {

    @JsonAdapter(MediaTypeDeserializer::class)
    enum class Type {
        @SerializedName("image")
        IMAGE,
        @SerializedName("gifv")
        GIFV,
        @SerializedName("video")
        VIDEO,
        @SerializedName("audio")
        AUDIO,
        @SerializedName("unknown")
        UNKNOWN
    }

    class MediaTypeDeserializer : JsonDeserializer<Type> {
        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, classOfT: java.lang.reflect.Type, context: JsonDeserializationContext): Type {
            return when (json.toString()) {
                "\"image\"" -> Type.IMAGE
                "\"gifv\"" -> Type.GIFV
                "\"video\"" -> Type.VIDEO
                "\"audio\"" -> Type.AUDIO
                else -> Type.UNKNOWN
            }
        }
    }

    /**
     * The meta data of an [Attachment].
     */
    @Parcelize
    data class MetaData(
            val focus: Focus?,
            val duration: Float?
    ) : Parcelable

    /**
     * The Focus entity, used to specify the focal point of an image.
     *
     * See here for more details what the x and y mean:
     *   https://github.com/jonom/jquery-focuspoint#1-calculate-your-images-focus-point
     */
    @Parcelize
    data class Focus(
        val x: Float,
        val y: Float
    ) : Parcelable
}
