package org.libre.agosto.p2play

import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_host.*
import org.libre.agosto.p2play.ajax.Auth
import org.libre.agosto.p2play.ajax.Client

class HostActivity : AppCompatActivity() {
    lateinit var settings: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    val client:Auth = Auth()
    val _db = Database(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)
        settings = PreferenceManager.getDefaultSharedPreferences(this)

        editor = settings.edit()

        button.setOnClickListener {
            getKeys(hostText.text.toString())
        }

        val host = settings.getString("hostP2play", "")
        val lastHost = settings.getString("last_host", "")
        if(host!=""){
            if(lastHost!=host){
                _db.logout()
                ManagerSingleton.logout()
                getKeys(host)
            }else{
                ManagerSingleton.url=host
                startApp()
            }
        }
    }



    fun saveHost(host: String){
        editor.putString("last_host",host)
        editor.putString("hostP2play",host)
        editor.apply()
        ManagerSingleton.Toast(getString(R.string.finallyMsg), this)
        ManagerSingleton.url=host
        startApp()
    }

    private fun getKeys(hostText: String){
        button.isEnabled = false
        var host = hostText.toString()
        host = host.replace("http://","")
        host = host.replace("https://","")
        host = host.replace("/","")
        ManagerSingleton.url = host
        AsyncTask.execute {
            if (Looper.myLooper()==null)
                Looper.prepare()

            val keys = client.getKeys()
            if(keys.client_id!=""){
                editor.putString("client_id",keys.client_id)
                editor.putString("client_secret",keys.client_secret)
                editor.apply()
                saveHost(host)
            }
            else{
                runOnUiThread {
                    ManagerSingleton.Toast(getString(R.string.errorMsg), this)
                    button.isEnabled = true
                }
            }
        }
    }

    private fun startApp(){
        runOnUiThread {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }
}
