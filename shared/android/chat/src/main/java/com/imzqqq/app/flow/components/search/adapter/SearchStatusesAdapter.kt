package com.imzqqq.app.flow.components.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.imzqqq.app.R
import com.imzqqq.app.flow.adapter.StatusViewHolder
import com.imzqqq.app.flow.entity.Status
import com.imzqqq.app.flow.interfaces.StatusActionListener
import com.imzqqq.app.flow.util.StatusDisplayOptions
import com.imzqqq.app.flow.viewdata.StatusViewData

class SearchStatusesAdapter(
    private val statusDisplayOptions: StatusDisplayOptions,
    private val statusListener: StatusActionListener
) : PagingDataAdapter<Pair<Status, StatusViewData.Concrete>, StatusViewHolder>(STATUS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_status, parent, false)
        return StatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.setupWithStatus(item.second, statusListener, statusDisplayOptions)
        }
    }

    fun item(position: Int): Pair<Status, StatusViewData.Concrete>? {
        return getItem(position)
    }

    companion object {

        val STATUS_COMPARATOR = object : DiffUtil.ItemCallback<Pair<Status, StatusViewData.Concrete>>() {
            override fun areContentsTheSame(oldItem: Pair<Status, StatusViewData.Concrete>, newItem: Pair<Status, StatusViewData.Concrete>): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Pair<Status, StatusViewData.Concrete>, newItem: Pair<Status, StatusViewData.Concrete>): Boolean =
                oldItem.second.id == newItem.second.id
        }
    }
}
