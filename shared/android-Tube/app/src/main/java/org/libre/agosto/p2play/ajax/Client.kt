package org.libre.agosto.p2play.ajax

import android.util.JsonReader
import android.util.Log
import org.libre.agosto.p2play.ManagerSingleton
import org.libre.agosto.p2play.models.HostModel
import java.io.InputStreamReader
import java.io.Reader
import java.net.HttpURLConnection
import java.net.URL

open class Client {


    protected fun _newCon(uri: String, method: String, token: String = ""): HttpURLConnection {
        val url = URL("https://${ManagerSingleton.url}/api/v1/$uri")
        val con = url.openConnection() as HttpURLConnection

        con.setRequestProperty("User-Agent", "P2play/0.1")
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        con.setRequestProperty("Accept", "*/*")

        if(token != ""){
            con.setRequestProperty("Authorization", "Bearer $token")
        }

        con.requestMethod=method
        con.connectTimeout=60000
        con.readTimeout=60000

        if(method == "POST")
            con.doOutput=true

        Log.d("Petition", url.toString())
        return con
    }

    fun getKeys():HostModel{
        val con = this._newCon("oauth-clients/local","GET")
        val keys = HostModel("","")
        try {
            if (con.responseCode == 200) {
                val response = InputStreamReader(con.inputStream)
                val data = JsonReader(response)
                data.beginObject()
                while (data.hasNext()) {
                    val key = data.nextName()
                    when (key.toString()) {
                        "client_id"->{
                            keys.client_id = data.nextString()
                        }
                        "client_secret"->{
                            keys.client_secret = data.nextString()
                        }
                        else->{
                            data.skipValue()
                        }
                    }
                }
                data.close()
            }
        } catch(err:Exception){
            err.printStackTrace()
        }

        con.disconnect()
        return keys
    }


}