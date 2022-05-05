package org.libre.agosto.p2play.models

import android.util.JsonReader
import android.util.JsonToken

class ChannelModel (
        var id: Int = 0,
        var url: String = "",
        var nameChannel: String = "",
        var followers: Int = 0,
        var host: String = "",
        var name: String = "",
        var description: String = "",
        var support: String = "",
        var channelImg: String = ""
) {
    fun getAccount(): String {
        return "$nameChannel@$host"
    }

    fun parseChannel(data: JsonReader) {
        data.beginObject()

        while (data.hasNext()) {

            when(data.nextName()){
                "id" -> this.id = data.nextInt()
                "url" -> this.url = data.nextString()
                "name" -> this.nameChannel = data.nextString()
                "host" -> this.host = data.nextString()
                "followersCount" -> this.followers = data.nextInt()
                "displayName" -> this.name = data.nextString()
                "description" -> {
                    if(data.peek() == JsonToken.STRING)
                        this.description = data.nextString()
                    else
                        data.skipValue()
                }
                "support" -> {
                    if(data.peek() == JsonToken.STRING)
                        this.support = data.nextString()
                    else
                        data.skipValue()
                }
                "avatar" -> {
                    if(data.peek() == JsonToken.BEGIN_OBJECT){
                        data.beginObject()
                        while (data.hasNext()){
                            when(data.nextName()){
                                "path" -> this.channelImg = data.nextString()
                                else -> data.skipValue()
                            }
                        }
                        data.endObject()
                    }
                    else
                        data.skipValue()
                }
                else -> data.skipValue()
            }
        }
        data.endObject()
    }
}