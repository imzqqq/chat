package org.libre.agosto.p2play.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.libre.agosto.p2play.*
import org.libre.agosto.p2play.models.VideoModel
import java.io.InputStream
import java.io.Serializable
import java.net.URL
import java.util.concurrent.TimeUnit

class VideosAdapter(private val myDataset: ArrayList<VideoModel>) :
        RecyclerView.Adapter<VideosAdapter.ViewHolder>() {


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val thumb: ImageView
        val userImg: ImageView
        val tittle: TextView
        val description: TextView
        val context: Context

        init {
            // Define click listener for the ViewHolder's View
            tittle = view.findViewById(R.id.tittleTxt)
            description = view.findViewById(R.id.descriptionTxt)
            thumb = view.findViewById(R.id.thumb)
            userImg = view.findViewById(R.id.userImg)
            context = view.context
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): VideosAdapter.ViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_video, parent, false) as View
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tittle.text = myDataset[position].name
        Picasso.get().load("https://"+ManagerSingleton.url+myDataset[position].thumbUrl).into(holder.thumb)
        holder.thumb.setOnClickListener {
            val intent = Intent(holder.context, ReproductorActivity::class.java)
            intent.putExtra("video", myDataset[position] as Serializable)
            holder.context.startActivity(intent)
        }

        holder.userImg.setOnClickListener {
            val intent = Intent(holder.context, ChannelActivity::class.java)
            intent.putExtra("channel", myDataset[position].getAccount())
            holder.context.startActivity(intent)
        }

        if(myDataset[position].userImageUrl!="")
            Picasso.get().load("https://"+ManagerSingleton.url+myDataset[position].userImageUrl).into(holder.userImg)
        else
            Picasso.get().load(R.drawable.default_avatar).into(holder.userImg)

        val viewsText = holder.context.getString(R.string.view_text)
        var timeText = holder.context.getString(R.string.timeSec_text)
        var timeString = myDataset[position].duration.toString()
        val seconds = myDataset[position].duration.toInt();
        if(seconds > 60 && seconds < (60 * 60)){
            timeText = holder.context.getString(R.string.timeMin_text)
            timeString = (seconds / 60).toString() + ":" + (seconds % 60).toString()
        }
        else if(seconds > (60 * 60)){
            timeText = holder.context.getString(R.string.timeHrs_text)
            timeString = (seconds / 60 / 60).toString() + ":" + (seconds / 60 % 60).toString()
        }

        holder.description.text = myDataset[position].username+" - "+myDataset[position].views+" "+viewsText+" - "+timeString+" "+timeText

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size

    fun clearData(){
        myDataset.clear()
        notifyDataSetChanged()
    }

    fun addData(newItems: ArrayList<VideoModel>){
        val lastPos = myDataset.size
        myDataset.addAll(newItems)
        notifyItemRangeInserted(lastPos, newItems.size)
    }


}