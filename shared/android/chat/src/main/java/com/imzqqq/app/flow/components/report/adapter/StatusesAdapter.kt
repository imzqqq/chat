package com.imzqqq.app.flow.components.report.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.imzqqq.app.flow.components.report.model.StatusViewState
import com.imzqqq.app.databinding.ItemReportStatusBinding
import com.imzqqq.app.flow.entity.Status
import com.imzqqq.app.flow.util.StatusDisplayOptions

class StatusesAdapter(
    private val statusDisplayOptions: StatusDisplayOptions,
    private val statusViewState: StatusViewState,
    private val adapterHandler: AdapterHandler
) : PagingDataAdapter<Status, StatusViewHolder>(STATUS_COMPARATOR) {

    private val statusForPosition: (Int) -> Status? = { position: Int ->
        if (position != RecyclerView.NO_POSITION) getItem(position) else null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding = ItemReportStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusViewHolder(
            binding, statusDisplayOptions, statusViewState, adapterHandler,
            statusForPosition
        )
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        getItem(position)?.let { status ->
            holder.bind(status)
        }
    }

    companion object {
        val STATUS_COMPARATOR = object : DiffUtil.ItemCallback<Status>() {
            override fun areContentsTheSame(oldItem: Status, newItem: Status): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Status, newItem: Status): Boolean =
                oldItem.id == newItem.id
        }
    }
}
