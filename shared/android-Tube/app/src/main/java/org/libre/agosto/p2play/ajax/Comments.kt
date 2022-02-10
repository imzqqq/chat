package org.libre.agosto.p2play.ajax

import android.util.JsonReader
import android.util.JsonToken
import android.util.Log
import org.libre.agosto.p2play.models.CommentaryModel
import java.io.InputStreamReader

class Comments: Client() {

    private fun parseCommentaries(data: JsonReader): ArrayList<CommentaryModel> {
        val commentaries = arrayListOf<CommentaryModel>()
        data.beginObject()
        while (data.hasNext()){
            when(data.nextName()) {
                "data" -> {
                    data.beginArray()
                    while (data.hasNext()) {
                        val comment = CommentaryModel()
                        comment.parseCommentary(data)
                        commentaries.add(comment)
                    }
                    data.endArray()
                }
                else -> data.skipValue()
            }
        }
        data.endObject()

        return commentaries
    }

    fun getCommentaries(videoId: Int): ArrayList<CommentaryModel> {
        var commentaries = arrayListOf<CommentaryModel>()
        val con = this._newCon("videos/$videoId/comment-threads", "GET")

        try {
            if (con.responseCode == 200) {
                val response = InputStreamReader(con.inputStream)
                val data = JsonReader(response)
                commentaries = parseCommentaries(data)
                data.close()
            }
        } catch(err:Exception){
            err.printStackTrace()
        }
        con.disconnect()
        return commentaries
    }

    fun makeCommentary(token: String, videoId: Int, text: String): Boolean {
        val con = this._newCon("videos/$videoId/comment-threads", "POST", token)
        val params = "text=$text"
        con.outputStream.write(params.toByteArray())

        var response: Boolean

        try {
            if (con.responseCode == 200) {
                con.disconnect()
                response = true
            }
            else{
                Log.d("Status", con.responseMessage)
                response = false
            }
        }
        catch (err: Exception){
            err.printStackTrace()
            response = false
        }

        con.disconnect()
        return response
    }

}