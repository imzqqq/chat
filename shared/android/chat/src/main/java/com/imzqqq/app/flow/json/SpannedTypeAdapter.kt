package com.imzqqq.app.flow.json

import android.text.Spanned
import android.text.SpannedString
import androidx.core.text.HtmlCompat
import androidx.core.text.parseAsHtml
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.imzqqq.app.flow.util.trimTrailingWhitespace
import java.lang.reflect.Type

class SpannedTypeAdapter : JsonDeserializer<Spanned>, JsonSerializer<Spanned?> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Spanned {
        /* Html.fromHtml returns trailing whitespace if the html ends in a </p> tag, which
         * all status contents do, so it should be trimmed. */
        return json.asString?.parseAsHtml()?.trimTrailingWhitespace() ?: SpannedString("")
    }

    override fun serialize(src: Spanned?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(HtmlCompat.toHtml(src!!, HtmlCompat.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL))
    }
}
