package org.libre.agosto.p2play.ajax

// import android.support.design.widget.Snackbar
import android.util.JsonReader
import android.util.JsonToken
import android.util.Log
import org.libre.agosto.p2play.ManagerSingleton
import org.libre.agosto.p2play.models.TokenModel
import org.libre.agosto.p2play.models.UserModel
import java.io.InputStreamReader

class Auth: Client() {
    private val stockParams = "grant_type=password"

    fun login(username: String, password: String, client_id: String, client_secret: String): TokenModel{
        val con = this._newCon("users/token","POST")
        val params = "$stockParams&username=$username&password=$password&client_id=$client_id&client_secret=$client_secret"
        con.outputStream.write(params.toByteArray())
        val token = TokenModel()

        try {

            if(con.responseCode==200){
                val response = InputStreamReader(con.inputStream)
                val data = JsonReader(response)
                data.beginObject()

                while(data.hasNext()){
                    val k = data.nextName()
                    when(k.toString()){
                        "access_token" -> token.token = data.nextString()
                        "refresh_token" -> token.refresh_token = data.nextString()
                        else -> data.skipValue()
                    }
                }

                data.endObject()
                data.close()
                token.status = 1

            }
            else{
                Log.d("Status", con.responseMessage)
            }
        }
        catch (err: Exception){
            err.printStackTrace()
            token.status = 0
        }

        con.disconnect()
        return token
    }

    fun register(username: String, password: String, email: String): Int{
        val con = this._newCon("users/register","POST")
        val params = "username=$username&password=$password&email=$email"
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

    fun refreshToken(token: TokenModel, client_id: String, client_secret: String): TokenModel{
        val con = this._newCon("users/token", "POST", token.token)
        val params = "refresh_token=${token.refresh_token}&response_type=code&grant_type=refresh_token&client_id=$client_id&client_secret=$client_secret"
        con.outputStream.write(params.toByteArray())
        // val token = TokenModel()

        try {
            if(con.responseCode==200){
                val response = InputStreamReader(con.inputStream)
                val data = JsonReader(response)
                data.beginObject()

                while(data.hasNext()){
                    val k = data.nextName()
                    when(k.toString()){
                        "access_token" -> token.token = data.nextString()
                        "refresh_token" -> token.refresh_token = data.nextString()
                        else -> data.skipValue()
                    }
                }

                data.endObject()
                data.close()
                token.status = 1

            }
            else{
                Log.d("Status", con.responseMessage)
            }
        }
        catch (err: Exception){
            err.printStackTrace()
            token.status = 0
        }

        con.disconnect()
        return token
    }

    fun me(token: String): UserModel{
        val con = this._newCon("users/me","GET", token)
        val user = UserModel()

        try {

            if(con.responseCode==200){
                val response = InputStreamReader(con.inputStream)
                val data = JsonReader(response)
                data.beginObject()

                while(data.hasNext()){
                    val k = data.nextName()
                    when(k.toString()){
                        "id" -> user.uuid = data.nextInt()
                        "username" -> user.username = data.nextString()
                        "email" -> user.email = data.nextString()
                        "displayNSFW" -> user.nsfw = data.nextBoolean()
                        "account" -> {
                            data.beginObject()
                            while (data.hasNext()){
                                val l = data.nextName()
                                when(l.toString()){
                                    "followersCount" -> user.followers = data.nextInt()
                                    "avatar" -> {
                                        if(data.peek() == JsonToken.BEGIN_OBJECT) {
                                            data.beginObject()
                                            while (data.hasNext()) {
                                                val m = data.nextName()
                                                when (m.toString()) {
                                                    "path" -> user.avatar = data.nextString()
                                                    else -> data.skipValue()
                                                }
                                            }
                                            data.endObject()
                                        }
                                        else{
                                            data.skipValue()
                                        }
                                    }
                                    else -> data.skipValue()
                                }
                            }
                            data.endObject()
                        }
                        else -> data.skipValue()
                    }
                }

                data.endObject()
                data.close()
                user.status = 1

            }
            else{
                Log.d("Status", con.responseMessage)
            }
        }
        catch (err: Exception){
            err.printStackTrace()
            user.status = 0
        }

        con.disconnect()
        return user
    }

}