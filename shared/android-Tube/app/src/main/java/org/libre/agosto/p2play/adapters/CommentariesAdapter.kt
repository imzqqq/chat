package org.libre.agosto.p2play.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.libre.agosto.p2play.*
import org.libre.agosto.p2play.models.CommentaryModel
import org.libre.agosto.p2play.models.VideoModel
import java.io.InputStream
import java.io.Serializable
import java.net.URL

@Suppress("DEPRECATION")
class CommentariesAdapter(private val myDataset: ArrayList<CommentaryModel>) :
        RecyclerView.Adapter<CommentariesAdapter.ViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val userImg: ImageView
        val username: TextView
        val commentary: TextView
        val context: Context

        init {
            // Define click listener for the ViewHolder's View
            username = view.findViewById(R.id.userTxt)
            commentary = view.findViewById(R.id.userCommentary)
            userImg = view.findViewById(R.id.userCommentImg)
            context = view.context
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CommentariesAdapter.ViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_commentary, parent, false) as View
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.username.text = myDataset[position].username

        // holder.userImg.setOnClickListener {
            // val intent = Intent(holder.context, ChannelActivity::class.java)
            // intent.putExtra("channel", myDataset[position])
            // holder.context.startActivity(intent)
        // }

        if(myDataset[position].userImageUrl!="")
            Picasso.get().load("https://"+ManagerSingleton.url+myDataset[position].userImageUrl).into(holder.userImg);

        holder.commentary.text = Html.fromHtml(myDataset[position].commentary)

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}