@file:Suppress("UNUSED_PARAMETER")

package com.imzqqq.app.flow.adapter

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.imzqqq.app.R
import com.imzqqq.app.flow.interfaces.StatusActionListener

/**
 * Placeholder for different timelines.
 * Either displays "load more" button or a progress indicator.
 **/
class PlaceholderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val loadMoreButton: Button = itemView.findViewById(R.id.button_load_more)
    private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

    @Suppress("UNUSED_PARAMETER")
    fun setup(listener: StatusActionListener, progress: Boolean) {
        loadMoreButton.visibility = if (progress) View.GONE else View.VISIBLE
        progressBar.visibility = if (progress) View.VISIBLE else View.GONE
        loadMoreButton.isEnabled = true

        /* MARK - imzqqq, FIXME
        loadMoreButton.setOnClickListener { v: View? ->
            loadMoreButton.isEnabled = false
            listener.onLoadMore(bindingAdapterPosition)
        }*/
        loadMoreButton.setOnClickListener {
            loadMoreButton.isEnabled = false
            listener.onLoadMore(bindingAdapterPosition)
        }
    }
}
