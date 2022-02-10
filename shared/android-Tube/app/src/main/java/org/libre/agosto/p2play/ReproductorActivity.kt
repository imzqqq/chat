package org.libre.agosto.p2play

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.widget.EditText
import android.widget.FrameLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_reproductor.*
import org.libre.agosto.p2play.adapters.CommentariesAdapter
import org.libre.agosto.p2play.ajax.Actions
import org.libre.agosto.p2play.ajax.Comments
import org.libre.agosto.p2play.ajax.Videos
import org.libre.agosto.p2play.models.CommentaryModel
import org.libre.agosto.p2play.models.VideoModel





@Suppress("NAME_SHADOWING")
class ReproductorActivity : AppCompatActivity() {
    lateinit var video: VideoModel
    private val _actions: Actions = Actions()
    private val client: Comments = Comments()
    private val videos: Videos = Videos()

    // Commentaries adapter values
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    @SuppressLint("SetJavaScriptEnabled", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reproductor)

        videoView.webChromeClient = WebClient()
        videoView.settings.javaScriptEnabled = true
        videoView.settings.allowContentAccess = true
        videoView.settings.javaScriptCanOpenWindowsAutomatically = true
        videoView.settings.allowFileAccess = true
        videoView.settings.allowFileAccessFromFileURLs = true
        videoView.settings.allowUniversalAccessFromFileURLs = true
        videoView.settings.domStorageEnabled = true

        try {
            this.video = this.intent.extras.getSerializable("video") as VideoModel
            tittleVideoTxt.text = this.video.name
            viewsTxt.text = "${this.video.views} ${getString(R.string.view_text)}"
            userTxt.text = this.video.username
            descriptionVideoTxt.text = this.video.description
            val haveDescription = this.video.description.endsWith("...")
            if (haveDescription) {
                showMoreBtn.visibility = View.VISIBLE
            }
            hostTxt.text = this.video.userHost

            // Check if user had profile image
            if (this.video.userImageUrl != "")
                Picasso.get().load("https://" + ManagerSingleton.url + this.video.userImageUrl).into(userImg)
            // Load the video
            videoView.loadUrl("https://" + ManagerSingleton.url + this.video.embedUrl)

        } catch (err: Exception) {
            err.printStackTrace()
        }

        viewManager = LinearLayoutManager(this)
        this.setDataComments(arrayListOf())

        this.getComments()

        subscribeBtn.setOnClickListener { subscribe() }
        likeLayout.setOnClickListener { rate("like") }
        dislikeLayout.setOnClickListener { rate("dislike") }
        commentaryBtn.setOnClickListener { makeComment() }
        showMoreBtn.setOnClickListener { getDescription() }
        shareLayout.setOnClickListener { shareIntent() }
        reportLayout.setOnClickListener { reportIntent() }

        userImg.setOnClickListener {
            val intent = Intent(this, ChannelActivity::class.java)
            intent.putExtra("channel", video.getAccount())
            startActivity(intent)
        }
    }

    private fun subscribe() {
        AsyncTask.execute {
            if (Looper.myLooper() == null)
                Looper.prepare()
            val res = this._actions.subscribe(ManagerSingleton.token.token, video.getAccount())
            if (res == 1) {
                runOnUiThread {
                    ManagerSingleton.Toast(getString(R.string.subscribeMsg), this)
                    this.changeSubscribeBtn(true)
                }
            }
        }
    }

    private fun unSubscribe() {
        AsyncTask.execute {
            if (Looper.myLooper() == null)
                Looper.prepare()
            val res = this._actions.unSubscribe(ManagerSingleton.token.token, video.getAccount())
            if (res == 1) {
                runOnUiThread {
                    ManagerSingleton.Toast(getString(R.string.unSubscribeMsg), this)
                    this.changeSubscribeBtn(false)
                }
            }
        }
    }

    private fun rate(rate: String) {
        AsyncTask.execute {
            if (Looper.myLooper() == null)
                Looper.prepare()
            val res = this._actions.rate(ManagerSingleton.token.token, this.video.id, rate)
            if (res == 1) {
                runOnUiThread {
                    ManagerSingleton.Toast(getString(R.string.rateMsg), this)
                    if (rate == "like") {
                        textViewLike.setTextColor(ContextCompat.getColor(this, R.color.colorLike))
                        textViewDislike.setTextColor(ContextCompat.getColor(this, R.color.primary_dark_material_light))
                    } else if (rate == "dislike") {
                        textViewDislike.setTextColor(ContextCompat.getColor(this, R.color.colorDislike))
                        textViewLike.setTextColor(ContextCompat.getColor(this, R.color.primary_dark_material_light))
                    }
                }
            }
        }
    }

    private fun getRate() {
        AsyncTask.execute {
            if (Looper.myLooper() == null)
                Looper.prepare()
            val rate = this._actions.getRate(ManagerSingleton.token.token, this.video.id)
            runOnUiThread {
                when (rate) {
                    "like" -> {
                        textViewLike.setTextColor(ContextCompat.getColor(this, R.color.colorLike))
                        textViewDislike.setTextColor(ContextCompat.getColor(this, R.color.primary_dark_material_light))
                    }
                    "dislike" -> {
                        textViewDislike.setTextColor(ContextCompat.getColor(this, R.color.colorDislike))
                        textViewLike.setTextColor(ContextCompat.getColor(this, R.color.primary_dark_material_light))
                    }
                    else -> {
                        textViewLike.setTextColor(ContextCompat.getColor(this, R.color.primary_dark_material_light))
                        textViewDislike.setTextColor(ContextCompat.getColor(this, R.color.primary_dark_material_light))
                    }
                }
            }
        }
    }

    private fun getSubscription() {
        val account = this.video.nameChannel + "@" + this.video.userHost
        AsyncTask.execute {
            if (Looper.myLooper() == null)
                Looper.prepare()
            val isSubscribed = this._actions.getSubscription(ManagerSingleton.token.token, account)
            runOnUiThread {
                this.changeSubscribeBtn(isSubscribed)
            }
        }
    }

    private fun changeSubscribeBtn(subscribed: Boolean) {
        if (subscribed) {
            subscribeBtn.text = getText(R.string.unSubscribeBtn)
            subscribeBtn.setOnClickListener { this.unSubscribe() }
        } else {
            subscribeBtn.text = getText(R.string.subscribeBtn)
            subscribeBtn.setOnClickListener { this.subscribe() }
        }
    }

    private fun setDataComments(data: ArrayList<CommentaryModel>) {
        // Set data for RecyclerView
        viewAdapter = CommentariesAdapter(data)

        recyclerView = findViewById<RecyclerView>(R.id.listCommentaries).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    private fun getComments() {
        AsyncTask.execute {
            val data = this.client.getCommentaries(this.video.id)
            runOnUiThread {
                this.setDataComments(data)
            }
        }
    }

    private fun makeComment() {
        if (commentaryText.text.toString() == "") {
            ManagerSingleton.Toast(getString(R.string.emptyCommentaryMsg), this)
            return
        }
        val text = commentaryText.text.toString()
        AsyncTask.execute {
            val res = this.client.makeCommentary(ManagerSingleton.token.token, this.video.id, text)
            runOnUiThread {
                if (res) {
                    ManagerSingleton.Toast(getString(R.string.makedCommentaryMsg), this)
                    commentaryText.text.clear()
                    this.getComments()
                } else {
                    ManagerSingleton.Toast(getString(R.string.errorCommentaryMsg), this)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (ManagerSingleton.user.status == 1) {
            this.getRate()
            this.getSubscription()
            actionsLayout.visibility = View.VISIBLE
            subscribeBtn.visibility = View.VISIBLE
            commentaryLayout.visibility = View.VISIBLE
            if (ManagerSingleton.user.avatar != "") {
                Picasso.get().load("https://" + ManagerSingleton.url + ManagerSingleton.user.avatar).into(userImgCom)
            }
        }
    }

    fun getDescription() {
        AsyncTask.execute {
            val fullDescription = this.videos.fullDescription(this.video.id)
            runOnUiThread {
                descriptionVideoTxt.text = fullDescription
                showMoreBtn.visibility = View.GONE
            }
        }
    }

    private fun shareIntent() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "${video.name} ${video.getVideoUrl()}")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.shareBtn)))
    }

    private fun reportIntent() {
        val builder = AlertDialog.Builder(this)
        // Get the layout inflater
        val dialog = layoutInflater.inflate(R.layout.report_dialog, null)

        val inputReason = dialog.findViewById<EditText>(R.id.reportText)

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialog)
            // Add action buttons
            .setPositiveButton(R.string.reportBtn) { _, _ ->
                val reason = inputReason.text.toString()
                reportVideo(reason)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.run { cancel() }
            }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun reportVideo(reason: String){
        AsyncTask.execute {
            val res = _actions.reportVideo(video.id, reason, ManagerSingleton.token.token)

            runOnUiThread {
                if(res) {
                    ManagerSingleton.Toast(getText(R.string.reportDialogMsg).toString(), this)
                }
                else {
                    ManagerSingleton.Toast(getText(R.string.errorMsg).toString(), this)
                }
            }
        }
    }

    internal inner class WebClient: WebChromeClient() {
        private var mCustomView: View? = null
        private var mCustomViewCallback: WebChromeClient.CustomViewCallback? = null
        private var mOriginalOrientation: Int = 0
        private var mOriginalSystemUiVisibility: Int = 0

        override fun getDefaultVideoPoster(): Bitmap? {
            AsyncTask.execute {
                this@ReproductorActivity._actions.watchVideo(this@ReproductorActivity.video.id, ManagerSingleton.token.token)
            }

            return if (mCustomView == null) {
                null
            } else BitmapFactory.decodeResource(this@ReproductorActivity.resources, 2130837573)
        }

        override fun onHideCustomView() {
            try {
                this@ReproductorActivity.nonFullScreen.visibility = View.VISIBLE
                this@ReproductorActivity.fullScreen.visibility = View.GONE
                this@ReproductorActivity.fullScreen.removeView(this.mCustomView)
                this.mCustomView = null

                this.mCustomViewCallback!!.onCustomViewHidden()
                this.mCustomViewCallback = null

                this@ReproductorActivity.requestedOrientation = this.mOriginalOrientation
                // this@ReproductorActivity.window.decorView.systemUiVisibility = this.mOriginalSystemUiVisibility

                val attrs = this@ReproductorActivity.window.attributes
                attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
                attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON.inv()
                window.attributes = attrs
                this@ReproductorActivity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
            catch (err: Exception){
                err.printStackTrace()
            }
        }

        override fun onShowCustomView(paramView: View, paramCustomViewCallback: WebChromeClient.CustomViewCallback) {
            if (this.mCustomView != null) {
                this.onHideCustomView()
                return
            }
            try {
                this.mCustomView = paramView
                this.mOriginalSystemUiVisibility = this@ReproductorActivity.window.decorView.systemUiVisibility
                this.mOriginalOrientation = this@ReproductorActivity.requestedOrientation
                this.mCustomViewCallback = paramCustomViewCallback
                val match_parent = WindowManager.LayoutParams.MATCH_PARENT

                this@ReproductorActivity.nonFullScreen.visibility = View.GONE
                this@ReproductorActivity.fullScreen.visibility = View.VISIBLE

                this@ReproductorActivity.fullScreen.addView(paramView, FrameLayout.LayoutParams(match_parent, match_parent))


                val attrs = this@ReproductorActivity.window.attributes
                attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
                attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                window.attributes = attrs
                this@ReproductorActivity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE

                this@ReproductorActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            }
            catch (err: Exception){
                err.printStackTrace()
            }
        }
    }
}
