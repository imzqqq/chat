package org.libre.agosto.p2play

import android.content.Context
import org.libre.agosto.p2play.models.TokenModel
import org.libre.agosto.p2play.models.UserModel

object ManagerSingleton {
    var url: String?= null
    var user: UserModel = UserModel()
    var token: TokenModel = TokenModel()
    var nfsw: Boolean = false
    var videos_count: Int = 0

    fun Toast(text: String?, context: Context) {
        android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_SHORT).show()
    }

    fun logout(){
        user = UserModel()
        token = TokenModel()
    }
}