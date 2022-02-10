package org.libre.agosto.p2play.models

import android.util.JsonReader
import android.util.JsonToken

class CommentaryModel (
        var id: Int = 0,
        var threadId: Int = 0,
        var userUuid: String = "",
        var username: String = "",
        var userImageUrl: String = "",
        var commentary: String = "",
        var userHost: String = "",
        var replies: Int = 0
) {
    fun parseCommentary(data: JsonReader) {
        data.beginObject()
        while (data.hasNext()) {
            val key = data.nextName()
            when (key.toString()) {
                "id" -> this.id = data.nextInt()
                "threadId" -> this.threadId = data.nextInt()
                "text" -> this.commentary = data.nextString()
                "totalReplies" -> this.replies = data.nextInt()
                "account" -> {
                    data.beginObject()
                    while (data.hasNext()){
                        val acKey = data.nextName()
                        when(acKey.toString()){
                            "displayName"-> this.username=data.nextString()
                            "avatar"-> {
                                if(data.peek() == JsonToken.BEGIN_OBJECT){
                                    data.beginObject()
                                    while (data.hasNext()){
                                        val avKey = data.nextName()
                                        when(avKey){
                                            "path"-> this.userImageUrl = data.nextString()
                                            else-> data.skipValue()
                                        }
                                    }
                                    data.endObject()
                                }
                                else
                                    data.skipValue()

                            }
                            "uuid" -> this.userUuid = data.nextString()
                            "host" -> this.userHost = data.nextString()
                            else -> data.skipValue()
                        }
                    }
                    data.endObject()
                }
                else -> data.skipValue()
            }
        }
        data.endObject()
    }
}