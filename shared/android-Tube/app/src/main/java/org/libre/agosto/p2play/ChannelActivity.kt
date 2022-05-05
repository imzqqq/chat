package org.libre.agosto.p2play

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_channel.*
import org.libre.agosto.p2play.adapters.VideosAdapter
import org.libre.agosto.p2play.ajax.Actions
import org.libre.agosto.p2play.ajax.Channels
import org.libre.agosto.p2play.ajax.Videos
import org.libre.agosto.p2play.models.ChannelModel
import org.libre.agosto.p2play.models.VideoModel

class ChannelActivity : AppCompatActivity() {
    private lateinit var channelId: String
    private lateinit var channel: ChannelModel
    private var isSubcribed: Boolean = false
    private val _channel = Channels()
    private val _videos = Videos()
    private val _actions = Actions()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<VideosAdapter.ViewHolder>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel)

        channelId = this.intent.extras.getString("channel")

        viewManager = LinearLayoutManager(this)

        subcriptionBtn.setOnClickListener {
            subscribeAction()
        }
    }

    override fun onResume() {
        super.onResume()

        getChannel()
        getSubscription()
        getVideos()

        if(ManagerSingleton.user.status == 1) {
            subcriptionBtn.visibility = View.VISIBLE
            getSubscription()
        }
    }

    private fun getChannel() {
        AsyncTask.execute {
            channel = _channel.getChannelInfo(channelId)
            runOnUiThread {
                usernameProfile.text = channel.name
                hostTxt.text = channel.host
                subcriptionsTxt.text = channel.followers.toString()
                if(channel.channelImg != "")
                    Picasso.get().load("https://${ManagerSingleton.url}${channel.channelImg}").into(channelImg)
            }
        }
    }

    private fun subscribe() {
        AsyncTask.execute {
            val res = _actions.subscribe(ManagerSingleton.token.token, channel.getAccount())
            runOnUiThread {
                if(res == 1){
                    subcriptionBtn.text = getString(R.string.unSubscribeBtn)
                    ManagerSingleton.Toast(getString(R.string.subscribeMsg), this)
                    getSubscription()
                }
                else {
                    ManagerSingleton.Toast(getString(R.string.errorMsg), this)
                }
            }
        }
    }

    private fun unSubscribe() {
        AsyncTask.execute {
            val res = _actions.unSubscribe(ManagerSingleton.token.token, channel.getAccount())
            runOnUiThread {
                if(res == 1){
                    subcriptionBtn.text = getString(R.string.subscribeBtn)
                    ManagerSingleton.Toast(getString(R.string.unSubscribeMsg), this)
                    getSubscription()
                }
                else {
                    ManagerSingleton.Toast(getString(R.string.errorMsg), this)
                }
            }
        }
    }

    private fun subscribeAction() {
        if(isSubcribed)
            unSubscribe()
        else
            subscribe()
    }

    private fun getSubscription() {
        AsyncTask.execute {
            isSubcribed = _actions.getSubscription(ManagerSingleton.token.token, channel.getAccount())
            runOnUiThread {
                if(isSubcribed){
                    subcriptionBtn.text = getText(R.string.unSubscribeBtn)
                }
                else {
                    subcriptionBtn.text = getText(R.string.subscribeBtn)
                }
            }
        }
    }

    private fun getVideos() {
        AsyncTask.execute {
            val videos = _videos.channelVideos(channel.getAccount(), 0)
            runOnUiThread {
                initRecycler(videos)
            }
        }
    }

    // Generic function for set data to RecyclerView
    private fun initRecycler(data: ArrayList<VideoModel>){
        // val data = arrayListOf<VideoModel>()
        viewAdapter = VideosAdapter(data)

        recyclerView = findViewById<RecyclerView>(R.id.listVideosChannel).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
        // swipeContainer.isRefreshing = false
    }
}