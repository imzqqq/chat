package org.libre.agosto.p2play

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.SearchView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.libre.agosto.p2play.adapters.VideosAdapter
import org.libre.agosto.p2play.ajax.Videos
import org.libre.agosto.p2play.models.VideoModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<VideosAdapter.ViewHolder>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val client: Videos = Videos()
    private lateinit var lastItem: MenuItem
    private lateinit var subItem: MenuItem
    lateinit var myMenu: Menu
    private val _db = Database(this)
    var section: String = ""
    var searchVal: String = ""
    var pagination = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        /* fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        } */

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        viewManager = LinearLayoutManager(this)

        // Init RecyclerView
        this.initRecycler()

        this.getTrengindVideos()

        swipeContainer.setOnRefreshListener {
            this.refresh()
        }

        Handler().postDelayed({
            // Title for nav_bar
            side_emailTxt?.text = getString(R.string.nav_header_subtitle) + " " + this.packageManager.getPackageInfo(this.packageName, 0).versionName
        }, 2000)
    }

    // Generic function for set data to RecyclerView
    private fun initRecycler(){
        val data = arrayListOf<VideoModel>()
        viewAdapter = VideosAdapter(data)

        recyclerView = findViewById<RecyclerView>(R.id.list).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

            this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if(!swipeContainer.isRefreshing){
                        if(!canScrollVertically(1)){
                            loadMore()
                        }
                    }
                }
            })
        }
        // swipeContainer.isRefreshing = false
    }

    private fun addVideos(videos: ArrayList<VideoModel>){
        this.swipeContainer.isRefreshing = true

        try {
            if(this.pagination == 0){
                (viewAdapter as VideosAdapter).clearData()
                recyclerView.scrollToPosition(0)
            }

            (viewAdapter as VideosAdapter).addData(videos)
        }catch (err: Exception){
            err.printStackTrace()
            ManagerSingleton.Toast(getString(R.string.errorMsg), this)
        }

        this.swipeContainer.isRefreshing = false
    }

    private fun refresh(){
        swipeContainer.isRefreshing = true
        this.pagination = 0
        when(section){
            "local" -> this.getLocalVideos()
            "popular" -> this.getPopularVideos()
            "trending" -> this.getTrengindVideos()
            "last" -> this.getLastVideos()
            "sub" -> this.getSubscriptionVideos()
            "search" -> this.searchVideos()
            "my_videos" -> {
                if(ManagerSingleton.token.token != "")
                    this.getMyVideos()
                else
                    this.getLastVideos()
            }
        }
    }

    private fun getSubscriptionVideos(){
        if(ManagerSingleton.user.status != 1){
            ManagerSingleton.Toast("Inicia session primero", this)
            startActivity(Intent(this, LoginActivity::class.java))
            return
        }
        swipeContainer.isRefreshing = true
        section = "sub"
        setTitle(R.string.title_subscriptions)
        AsyncTask.execute {
            val videos = client.videoSubscriptions(ManagerSingleton.token.token, this.pagination)
            runOnUiThread {
                this.addVideos(videos)
            }
        }
    }

    // Last videos
    private fun getLastVideos(){
        swipeContainer.isRefreshing = true
        section = "last"
        setTitle(R.string.title_recent)
        AsyncTask.execute {
            val videos = client.getLastVideos(this.pagination)
            runOnUiThread {
                this.addVideos(videos)
            }
        }
    }

    // Popular videos
    private fun getPopularVideos(){
        swipeContainer.isRefreshing = true
        section = "popular"
        setTitle(R.string.title_popular)
        AsyncTask.execute {
            val videos = client.getPopularVideos(this.pagination)
            runOnUiThread {
                this.addVideos(videos)
            }
        }
    }

    // Trending videos
    private fun getTrengindVideos(){
        swipeContainer.isRefreshing = true
        section = "trending"
        setTitle(R.string.title_trending)
        AsyncTask.execute {
            val videos = client.getTrendingVideos(this.pagination)
            runOnUiThread {
                this.addVideos(videos)
            }
        }
    }

    // Local videos
    private fun getLocalVideos(){
        swipeContainer.isRefreshing = true
        section = "local"
        setTitle(R.string.title_local)
        AsyncTask.execute {
            val videos = client.getLocalVideos(this.pagination)
            runOnUiThread {
                this.addVideos(videos)
            }
        }
    }

    // Videos of user
    private fun getMyVideos(){
        swipeContainer.isRefreshing = true
        section = "my_videos"
        setTitle(R.string.title_myVideos)
        AsyncTask.execute {
            val videos = client.myVideos(ManagerSingleton.token.token, this.pagination)
            runOnUiThread {
                this.addVideos(videos)
            }
        }
    }

    // Videos history of user
    private fun getHistory(){
        swipeContainer.isRefreshing = true
        section = "my_videos"
        setTitle(R.string.nav_history)
        AsyncTask.execute {
            val videos = client.videoHistory(ManagerSingleton.token.token, this.pagination)
            runOnUiThread {
                this.addVideos(videos)
            }
        }
    }

    // Most liked
    private fun getMostLiked(){
        swipeContainer.isRefreshing = true
        section = "liked"
        setTitle(R.string.nav_likes)
        AsyncTask.execute {
            val videos = client.getMostLikedVideos(this.pagination)
            runOnUiThread {
                this.addVideos(videos)
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        else if(!section.equals("trending")) {
            this.getTrengindVideos()
        }
        else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                if(!p0.isNullOrBlank()){
                    searchVal = p0
                    pagination = 0
                    searchView.onActionViewCollapsed()
                    searchVideos()
                }
                return true
            }

        })


        myMenu = menu
        setSideData()
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        myMenu = menu!!

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_login -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_logout -> {
                logout()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        // if(::lastItem.isInitialized){
            // lastItem.isChecked = false
        // }
        lastItem = item
        pagination = 0

        // item.isChecked = true
        when (item.itemId) {
            R.id.nav_subscriptions -> getSubscriptionVideos()
            R.id.nav_popular -> getPopularVideos()
            R.id.nav_trending -> getTrengindVideos()
            R.id.nav_recent -> getLastVideos()
            R.id.nav_local -> getLocalVideos()
            R.id.nav_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_history -> getHistory()
            R.id.nav_myVideos -> getMyVideos()
            R.id.nav_likes -> getMostLiked()
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onResume() {
        super.onResume()
        setSideData()
    }

    private fun setSideData(){
        if(ManagerSingleton.user.status == 1){
            nav_view.menu.findItem(R.id.ml).isVisible = true

            side_usernameTxt?.text = ManagerSingleton.user.username
            side_emailTxt?.text = ManagerSingleton.user.email
            if(ManagerSingleton.user.avatar!="" && side_imageView != null) {
                Picasso.get().load("https://" + ManagerSingleton.url + ManagerSingleton.user.avatar).into(side_imageView)
            }
//            side_imageView?.setOnClickListener {
//                pagination = 0
//                getMyVideos()
//                drawer_layout.closeDrawer(GravityCompat.START)
//            }
            if(::myMenu.isInitialized){
                myMenu.findItem(R.id.action_login).isVisible = false
                myMenu.findItem(R.id.action_logout).isVisible = true

            }
        }
    }

    private fun logout(){
        if(::myMenu.isInitialized){
            myMenu.findItem(R.id.action_login).isVisible = true
            myMenu.findItem(R.id.action_logout).isVisible = false
        }
        nav_view.menu.findItem(R.id.ml).isVisible = false
        side_usernameTxt?.text = getString(R.string.nav_header_title)
        side_emailTxt?.text = getString(R.string.nav_header_subtitle) + " " + this.packageManager.getPackageInfo(this.packageName, 0).versionName
        side_imageView?.setImageResource(R.drawable.default_avatar)
        side_imageView?.setOnClickListener {  }
        _db.logout()
        ManagerSingleton.logout()

        this.refresh()
        ManagerSingleton.Toast(getString(R.string.logout_msg), this)

    }

    private fun loadMore(){
        swipeContainer.isRefreshing = true
        this.pagination += ManagerSingleton.videos_count

        when(section){
            "local" -> this.getLocalVideos()
            "popular" -> this.getPopularVideos()
            "trending" -> this.getTrengindVideos()
            "last" -> this.getLastVideos()
            "sub" -> this.getSubscriptionVideos()
            "search" -> this.searchVideos()
            "my_videos" -> {
                if(ManagerSingleton.token.token != "")
                    this.getMyVideos()
                else
                    this.getLastVideos()
            }
            "liked" -> this.getMostLiked()
        }
    }

    private fun searchVideos(){
        swipeContainer.isRefreshing = true
        section = "search"
        this.title = this.searchVal
        AsyncTask.execute {
            val videos = client.search(this.searchVal, this.pagination)
            runOnUiThread {
                this.addVideos(videos)
            }
        }
    }

}
