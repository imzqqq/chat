package org.libre.agosto.p2play.models

import android.util.JsonReader
import android.util.JsonToken
import java.io.Serializable

class VideoModel(
        var id: Int = 0,
        var uuid: String = "",
        var name: String = "",
        var description: String = "",
        var thumbUrl: String = "",
        var userImageUrl: String = "",
        var embedUrl: String = "",
        var duration: Number = 0,
        var username: String = "",
        var views: Number = 0,
        var userUuid: String = "",
        var userHost: String = "",
        var nameChannel: String = ""
):Serializable {
    fun getAccount(): String {
        return "$nameChannel@$userHost"
    }

    fun getVideoUrl(): String {
        return "https://$userHost/videos/watch/$uuid"
    }

    fun parseVideo(data: JsonReader){
        data.beginObject()
        while (data.hasNext()){
            val key = data.nextName()
            when (key.toString()) {
                "id"-> this.id = data.nextInt()
                "uuid" -> this.uuid = data.nextString()
                "name"->{
                    this.name= data.nextString()
                }
                "description"->{
                    if(data.peek() == JsonToken.STRING)
                        this.description = data.nextString()
                    else
                        data.skipValue()
                }
                "duration"->{
                    this.duration = data.nextInt()
                }
                "thumbnailPath"->{
                    this.thumbUrl = data.nextString()
                }
                "embedPath"->{
                    this.embedUrl = data.nextString()
                }
                "views"->{
                    this.views = data.nextInt()
                }
                "channel"->{
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
                            "name" -> this.nameChannel = data.nextString()
                            else-> data.skipValue()
                        }
                    }
                    data.endObject()
                }
                else->{
                    data.skipValue()
                }
            }
        }
        data.endObject()
    }
}