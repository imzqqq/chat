package org.libre.agosto.p2play.ajax

import android.util.JsonReader
import android.util.JsonToken
import org.libre.agosto.p2play.models.ChannelModel
import org.libre.agosto.p2play.models.CommentaryModel
import java.io.InputStreamReader

class Channels: Client() {

    private fun parseChannel(data: JsonReader): ChannelModel{
        val channel = ChannelModel()

        data.close()

        return channel
    }

    fun getChannelInfo(account: String): ChannelModel {
        val con = this._newCon("video-channels/$account", "GET")
        var channel = ChannelModel()
        try {
            if(con.responseCode == 200){
                val response = InputStreamReader(con.inputStream)
                val data = JsonReader(response)
                channel.parseChannel(data)
                data.close()
            }
        }catch (err: Exception) {
            err.printStackTrace()
        }

        return channel
    }
}