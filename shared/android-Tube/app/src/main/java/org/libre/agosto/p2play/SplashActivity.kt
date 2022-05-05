package org.libre.agosto.p2play

import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import org.libre.agosto.p2play.ajax.Auth
import java.lang.Exception

class SplashActivity : AppCompatActivity() {
    lateinit var settings: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    val client: Auth = Auth()
    val _db = Database(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        settings = PreferenceManager.getDefaultSharedPreferences(this)

        ManagerSingleton.nfsw = settings.getBoolean("show_nfsw", false)
        ManagerSingleton.videos_count = settings.getString("videos_count", "15").toInt()


        val host = settings.getString("hostP2play","")
        val lastHost = settings.getString("last_host","")
        if(host != ""){
            if(lastHost != host){
                _db.logout()
                Handler().postDelayed({
                    startHostActivity()
                }, 2000)
            }else{
                ManagerSingleton.url = host
                checkUser()
            }
        }
        else{
            Handler().postDelayed({
                startHostActivity()
            }, 2000)
        }
    }

    private fun checkUser(){
        Log.d("was", "Chequed")
        try {
            val token = _db.getToken()
            val user = _db.getUser()
            AsyncTask.execute {
                if (Looper.myLooper() == null)
                    Looper.prepare()


                if (token.status == 1 && user.status == 1) {
                    val client_id = settings.getString("client_id", "")
                    val client_secret = settings.getString("client_secret", "")

                    val newToken = client.refreshToken(token, client_id, client_secret)

                    when (token.status.toString()) {
                        "1" -> {
                            _db.newToken(newToken)
                            ManagerSingleton.token = newToken
                            ManagerSingleton.user = user
                        }
                        else -> _db.logout()
                    }
                } else {
                    _db.logout()
                }


                startApp()
                Log.d("Aqui", "81")

            }
        }
        catch (err: Exception){
            err.printStackTrace()
            Log.d("Aqui", "89")
            Handler().postDelayed({
                startApp()
            }, 2000)
        }
    }

    private fun startApp() {
        runOnUiThread {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    private fun startHostActivity() {
        runOnUiThread {
            val intent = Intent(this, HostActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }
}