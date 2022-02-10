package com.imzqqq.app.flow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imzqqq.app.R
import com.imzqqq.app.flow.interfaces.StatusActionListener
import com.imzqqq.app.flow.util.StatusDisplayOptions
import com.imzqqq.app.flow.viewdata.StatusViewData

class ThreadAdapter(
        private val statusDisplayOptions: StatusDisplayOptions,
        private val statusActionListener: StatusActionListener
) : RecyclerView.Adapter<StatusBaseViewHolder>() {
    private val statuses = mutableListOf<StatusViewData.Concrete>()
    var detailedStatusPosition: Int = RecyclerView.NO_POSITION
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusBaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_STATUS -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_status, parent, false)
                StatusViewHolder(view)
            }
            VIEW_TYPE_STATUS_DETAILED -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_status_detailed, parent, false)
                StatusDetailedViewHolder(view)
            }
            else -> error("Unknown item type: $viewType")
        }
    }

    override fun onBindViewHolder(viewHolder: StatusBaseViewHolder, position: Int) {
        val status = statuses[position]
        viewHolder.setupWithStatus(status, statusActionListener, statusDisplayOptions)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == detailedStatusPosition) {
            VIEW_TYPE_STATUS_DETAILED
        } else {
            VIEW_TYPE_STATUS
        }
    }

    override fun getItemCount(): Int = statuses.size

    fun setStatuses(statuses: List<StatusViewData.Concrete>?) {
        this.statuses.clear()
        this.statuses.addAll(statuses!!)
        notifyDataSetChanged()
    }

    fun addItem(position: Int, statusViewData: StatusViewData.Concrete) {
        statuses.add(position, statusViewData)
        notifyItemInserted(position)
    }

    fun clearItems() {
        val oldSize = statuses.size
        statuses.clear()
        detailedStatusPosition = RecyclerView.NO_POSITION
        notifyItemRangeRemoved(0, oldSize)
    }

    fun addAll(position: Int, statuses: List<StatusViewData.Concrete>) {
        this.statuses.addAll(position, statuses)
        notifyItemRangeInserted(position, statuses.size)
    }

    fun addAll(statuses: List<StatusViewData.Concrete>) {
        val end = statuses.size
        this.statuses.addAll(statuses)
        notifyItemRangeInserted(end, statuses.size)
    }

    fun removeItem(position: Int) {
        statuses.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clear() {
        statuses.clear()
        detailedStatusPosition = RecyclerView.NO_POSITION
        notifyDataSetChanged()
    }

    fun setItem(position: Int, status: StatusViewData.Concrete, notifyAdapter: Boolean) {
        statuses[position] = status
        if (notifyAdapter) {
            notifyItemChanged(position)
        }
    }

    fun getItem(position: Int): StatusViewData.Concrete? = statuses.getOrNull(position)

    fun setDetailedStatusPosition(position: Int) {
        if (position != detailedStatusPosition &&
            detailedStatusPosition != RecyclerView.NO_POSITION
        ) {
            val prior = detailedStatusPosition
            detailedStatusPosition = position
            notifyItemChanged(prior)
        } else {
            detailedStatusPosition = position
        }
    }

    companion object {
        private const val VIEW_TYPE_STATUS = 0
        private const val VIEW_TYPE_STATUS_DETAILED = 1
    }
}
