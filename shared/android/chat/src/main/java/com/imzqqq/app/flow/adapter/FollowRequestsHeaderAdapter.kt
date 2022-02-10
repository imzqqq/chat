package com.imzqqq.app.flow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.imzqqq.app.R

class FollowRequestsHeaderAdapter(private val instanceName: String, private val accountLocked: Boolean) : RecyclerView.Adapter<HeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_follow_requests_header, parent, false) as TextView
        return HeaderViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: HeaderViewHolder, position: Int) {
        viewHolder.textView.text = viewHolder.textView.context.getString(R.string.follow_requests_info, instanceName)
    }

    override fun getItemCount() = if (accountLocked) 0 else 1
}

class HeaderViewHolder(var textView: TextView) : RecyclerView.ViewHolder(textView)
