package org.libre.agosto.p2play

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        aboutUrl.text = "https://"+ManagerSingleton.url+"/about/instance"

        aboutLabel.text = aboutLabel.text.toString() + " " + this.packageManager.getPackageInfo(this.packageName, 0).versionName
    }
}
