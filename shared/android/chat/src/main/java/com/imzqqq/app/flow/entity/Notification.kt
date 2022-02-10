package com.imzqqq.app.flow.entity

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.annotations.JsonAdapter

data class Notification(
        val type: Type,
        val id: String,
        val account: Account,
        val status: Status?
) {

    @JsonAdapter(NotificationTypeAdapter::class)
    enum class Type(val presentation: String) {
        UNKNOWN("unknown"),
        MENTION("mention"),
        REBLOG("reblog"),
        FAVOURITE("favourite"),
        FOLLOW("follow"),
        FOLLOW_REQUEST("follow_request"),
        POLL("poll"),
        STATUS("status");

        companion object {

            @JvmStatic
            fun byString(s: String): Type {
                values().forEach {
                    if (s == it.presentation)
                        return it
                }
                return UNKNOWN
            }
            val asList = listOf(MENTION, REBLOG, FAVOURITE, FOLLOW, FOLLOW_REQUEST, POLL, STATUS)
        }

        override fun toString(): String {
            return presentation
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Notification) {
            return false
        }
        val notification = other as Notification?
        return notification?.id == this.id
    }

    class NotificationTypeAdapter : JsonDeserializer<Type> {

        @Throws(JsonParseException::class)
        override fun deserialize(
            json: JsonElement,
            typeOfT: java.lang.reflect.Type,
            context: JsonDeserializationContext
        ): Type {
            return Type.byString(json.asString)
        }
    }

    /** Helper for Java */
    fun copyWithStatus(status: Status?): Notification = copy(status = status)

    // for Pleroma compatibility that uses Mention type
    fun rewriteToStatusTypeIfNeeded(accountId: String): Notification {
        if (type == Type.MENTION && status != null) {
            return if (status.mentions.any {
                it.id == accountId
            }
            ) this else copy(type = Type.STATUS)
        }
        return this
    }
}
