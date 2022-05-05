package org.libre.agosto.p2play

import android.content.SharedPreferences
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_register.*
import org.libre.agosto.p2play.ajax.Auth

class RegisterActivity : AppCompatActivity() {
    private val _auth = Auth()
    lateinit var settings: SharedPreferences
    lateinit var client_id: String
    lateinit var client_secret: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setTitle(R.string.registerActionBtn)

        settings = PreferenceManager.getDefaultSharedPreferences(this)
        client_id = settings.getString("client_id", "")
        client_secret = settings.getString("client_secret", "")

        registerBtn.setOnClickListener { registerUser() }
    }

    fun registerUser(){
        registerBtn.isEnabled = false
        val username = userText2.text.toString()
        val password = passwordText2.text.toString()
        val email = emailText.text.toString()
        AsyncTask.execute {
            if (Looper.myLooper()==null)
                Looper.prepare()

            val res = _auth.register(username, password, email)
            Log.d("Res register", res.toString())
            runOnUiThread {
                when (res) {
                    1 -> {
                        ManagerSingleton.Toast(getString(R.string.registerSuccess_msg), this)
                        finish()
                    }
                    0 -> ManagerSingleton.Toast(getString(R.string.registerFailed_msg), this)
                    -1 -> ManagerSingleton.Toast(getString(R.string.registerError_msg), this)
                }
                registerBtn.isEnabled = true
            }
        }
    }
}
