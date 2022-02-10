package org.libre.agosto.p2play

import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_login.*
import org.libre.agosto.p2play.ajax.Auth

class LoginActivity : AppCompatActivity() {
    private val _auth = Auth()
    lateinit var settings: SharedPreferences
    lateinit var client_id: String
    lateinit var client_secret: String
    private lateinit var _db: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setTitle(R.string.action_login)
        _db = Database(this)

        settings = PreferenceManager.getDefaultSharedPreferences(this)
        client_id = settings.getString("client_id", "")
        client_secret = settings.getString("client_secret", "")

        registerActionBtn.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
        loginBtn.setOnClickListener { tryLogin() }
    }

    fun tryLogin(){
        loginBtn.isEnabled = false;
        val username = userText.text.toString()
        val password = passwordText.text.toString()

        AsyncTask.execute {
            if (Looper.myLooper()==null)
                Looper.prepare()

            val token = _auth.login(username, password, client_id, client_secret)

            // Log.d("token", token.token )
            // Log.d("status", token.status.toString() )


            when(token.status.toString()){
                "1" -> {
                    _db.newToken(token)
                    ManagerSingleton.token = token
                    getUser()
                }
                "0" -> {
                    runOnUiThread {
                        ManagerSingleton.Toast(getString(R.string.loginError_msg), this)
                    }
                }
                "-1" -> {
                    runOnUiThread {
                        loginBtn.isEnabled = true
                        ManagerSingleton.Toast(getString(R.string.loginFailed_msg), this)
                    }
                }
            }

        }
    }

    fun getUser(){
        val user = _auth.me(ManagerSingleton.token.token)
        if(user.status == 1){
            _db.newUser(user)
            ManagerSingleton.user = user
            runOnUiThread {
                ManagerSingleton.Toast(getString(R.string.loginSuccess_msg), this)
                finish()
            }
        }
        else{
            runOnUiThread {
                ManagerSingleton.Toast(getString(R.string.loginError_msg), this)
            }
        }
    }
}
