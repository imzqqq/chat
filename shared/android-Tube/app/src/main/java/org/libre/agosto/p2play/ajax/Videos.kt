package org.libre.agosto.p2play.ajax

import android.util.JsonReader
import android.util.JsonToken
import org.libre.agosto.p2play.ManagerSingleton
import org.libre.agosto.p2play.models.VideoModel
import java.io.InputStreamReader

class Videos: Client() {

    private fun parseVideos(data: JsonReader): ArrayList<VideoModel>{
        val videos = arrayListOf<VideoModel>()
        data.beginObject()
        while (data.hasNext()){
            when(data.nextName()){
                "data"->{
                    data.beginArray()
                    while (data.hasNext()) {
                        val video = VideoModel()
                        video.parseVideo(data)
                        videos.add(video)
                    }
                    data.endArray()
                }
                else-> data.skipValue()
            }
        }
        data.endObject()

        return videos
    }

    private fun getVideos(start:Int, sort:String = "-publishedAt", filter:String = ""):ArrayList<VideoModel>{
        val nsfw = ManagerSingleton.nfsw
        val count = ManagerSingleton.videos_count
        var params = "start=$start&count=$count&sort=$sort&nsfw=$nsfw"
        if(filter != "")
            params+="&filter=$filter"
        val con = this._newCon("videos?$params","GET")
        var videos = arrayListOf<VideoModel>()
        try {
            if (con.responseCode == 200) {
                val response = InputStreamReader(con.inputStream)
                val data = JsonReader(response)
                videos = parseVideos(data)
                data.close()
            }
        } catch(err:Exception){
            err.printStackTrace()
        }
        con.disconnect()
        return videos
    }

    fun getLastVideos(start:Int = 0): ArrayList<VideoModel>{
        return this.getVideos(start)
    }

    fun getPopularVideos(start:Int = 0): ArrayList<VideoModel>{
        return this.getVideos(start,"-views")
    }

    fun getTrendingVideos(start:Int = 0): ArrayList<VideoModel>{
        return this.getVideos(start,"-trending")
    }

    fun getLocalVideos(start:Int = 0): ArrayList<VideoModel>{
        return this.getVideos(start,"-publishedAt", "local")
    }

    fun myVideos(token: String, start: Int = 0): ArrayList<VideoModel>{
        val count = ManagerSingleton.videos_count
        val params = "start=$start&count=$count"
        val con = this._newCon("users/me/videos?$params","GET", token)
        var videos = arrayListOf<VideoModel>()
        try {
            if (con.responseCode == 200) {
                val response = InputStreamReader(con.inputStream)
                val data = JsonReader(response)
                videos = parseVideos(data)
                data.close()
            }
        } catch(err:Exception){
            err.printStackTrace()
        }

        con.disconnect()
        return videos
    }

    fun videoSubscriptions(token: String, start: Int = 0): ArrayList<VideoModel>{
        val count = ManagerSingleton.videos_count
        val params = "start=$start&count=$count"
        val con = this._newCon("users/me/subscriptions/videos?$params","GET", token)
        var videos = arrayListOf<VideoModel>()
        try {
            if (con.responseCode == 200) {
                val response = InputStreamReader(con.inputStream)
                val data = JsonReader(response)
                videos = parseVideos(data)
                data.close()
            }
        } catch(err:Exception){
            err.printStackTrace()
        }

        con.disconnect()
        return videos
    }

    fun videoHistory(token: String, start: Int = 0): ArrayList<VideoModel>{
        val count = ManagerSingleton.videos_count
        val params = "start=$start&count=$count"
        val con = this._newCon("users/me/history/videos?$params","GET", token)
        var videos = arrayListOf<VideoModel>()
        try {
            if (con.responseCode == 200) {
                val response = InputStreamReader(con.inputStream)
                val data = JsonReader(response)
                videos = parseVideos(data)
                data.close()
            }
        } catch(err:Exception){
            err.printStackTrace()
        }

        con.disconnect()
        return videos
    }

    fun search(text: String, start: Int = 0): ArrayList<VideoModel>{
        val count = ManagerSingleton.videos_count
        val nsfw = ManagerSingleton.nfsw
        val params = "search=$text&start=$start&count=$count&nsfw=$nsfw"
        val con = this._newCon("search/videos?$params","GET")
        var videos = arrayListOf<VideoModel>()
        try {
            if (con.responseCode == 200) {
                val response = InputStreamReader(con.inputStream)
                val data = JsonReader(response)
                videos = parseVideos(data)
                data.close()
            }
        } catch(err:Exception){
            err.printStackTrace()
        }
        con.disconnect()
        return videos
    }

    fun fullDescription(videoId: Int): String{
        val con = this._newCon("videos/$videoId/description","GET")
        var description = ""
        try {
            if (con.responseCode == 200) {
                val response = InputStreamReader(con.inputStream)
                val data = JsonReader(response)

                data.beginObject()
                while (data.hasNext()){
                    val name = data.nextName()
                    when(name){
                        "description" -> description = data.nextString()
                        else -> data.skipValue()
                    }
                }
                data.endObject()
                data.close()
            }
        } catch(err:Exception){
            err.printStackTrace()
            description = "Error!"
        }
        con.disconnect()
        return description
    }

    fun channelVideos(account: String, start: Int): ArrayList<VideoModel> {
        val count = ManagerSingleton.videos_count
        val params = "start=$start&count=$count"
        val con = this._newCon("video-channels/$account/videos?$params","GET")
        var videos = arrayListOf<VideoModel>()
        try {
            if (con.responseCode == 200) {
                val response = InputStreamReader(con.inputStream)
                val data = JsonReader(response)
                videos = parseVideos(data)
                data.close()
            }
        } catch(err:Exception){
            err.printStackTrace()
        }

        con.disconnect()
        return videos
    }

    fun getMostLikedVideos(start:Int = 0): ArrayList<VideoModel>{
        return this.getVideos(start,"-likes")
    }
}