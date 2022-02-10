package org.libre.agosto.p2play.ajax

import android.util.JsonReader
import java.io.InputStreamReader

class Actions: Client() {

    fun subscribe(token: String, account: String):Int{
        val con = this._newCon("users/me/subscriptions","POST", token)
        val params:String= "uri=$account"
        con.outputStream.write(params.toByteArray())
        var response = 0

        try {
            if (con.responseCode == 204) {
                response = 1
            }
        }
        catch (err: Exception){
            err.printStackTrace()
            response = -1
        }

        con.disconnect()
        return response
    }

    fun unSubscribe(token: String, account: String):Int{
        val con = this._newCon("users/me/subscriptions/$account","DELETE", token)
        var response = 0

        try {
            if (con.responseCode == 204) {
                response = 1
            }
        }
        catch (err: Exception){
            err.printStackTrace()
            response = -1
        }

        con.disconnect()
        return response
    }

    fun getSubscription(token: String, account: String): Boolean{
        val con = this._newCon("users/me/subscriptions/exist?uris=$account","GET", token)
        var isSubscribed = false

        try {
            if (con.responseCode == 200) {
                val response = InputStreamReader(con.inputStream)
                val data = JsonReader(response)
                data.beginObject()
                while (data.hasNext()){
                    val key = data.nextName()
                    when (key.toString()) {
                        account->{
                            isSubscribed = data.nextBoolean()
                        }
                        else->{
                            data.skipValue()
                        }
                    }
                }
                data.close()
            }
        }
        catch (err: Exception){
            err.printStackTrace()
            isSubscribed = false
        }

        con.disconnect()
        return isSubscribed
    }

    fun rate(token: String, id_video: Int, rate: String):Int{
        val con = this._newCon("videos/$id_video/rate","PUT", token)
        val params = "rating=$rate"
        con.outputStream.write(params.toByteArray())
        var response = 0

        try {
            if (con.responseCode == 204) {
                response = 1
            }
        }
        catch (err: Exception){
            err.printStackTrace()
            response = -1
        }

        con.disconnect()
        return response
    }

    fun getRate(token: String, id_video: Int):String{
        val con = this._newCon("users/me/videos/$id_video/rating","GET", token)
        var rating = "none"

        try {
            if (con.responseCode == 200) {
                val response = InputStreamReader(con.inputStream)
                val data = JsonReader(response)
                data.beginObject()
                while (data.hasNext()){
                    val key = data.nextName()
                    when (key.toString()) {
                        "rating"->{
                            rating = data.nextString()
                        }
                        else->{
                            data.skipValue()
                        }
                    }
                }
                con.disconnect()
            }
        }
        catch (err: Exception){
            err.printStackTrace()
            rating = "none"
        }

        con.disconnect()
        return rating
    }

    fun reportVideo(videoId: Int, reason: String, token: String): Boolean {
        val con = this._newCon("videos/$videoId/abuse", "POST", token)
        val params = "reason=$reason"
        con.outputStream.write(params.toByteArray())

        var response = false
        try {
            if(con.responseCode == 200){
                response = true
            }
        } catch (err: Exception) {
            err.printStackTrace()
            response = false
        }

        return response
    }

    fun watchVideo(videoId: Int, token: String): Boolean {
        val con = this._newCon("videos/$videoId/watching", "PUT", token)
        val params = "currentTime=1"
        con.outputStream.write(params.toByteArray())

        var response = false
        try {
            if(con.responseCode == 204){
                response = true
            }
        } catch (err: Exception) {
            err.printStackTrace()
            response = false
        }

        return response
    }
}