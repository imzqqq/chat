package com.imzqqq.app.flow.components.scheduled

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.imzqqq.app.databinding.ItemScheduledTootBinding
import com.imzqqq.app.flow.entity.ScheduledStatus
import com.imzqqq.app.flow.util.BindingHolder

interface ScheduledTootActionListener {
    fun edit(item: ScheduledStatus)
    fun delete(item: ScheduledStatus)
}

class ScheduledTootAdapter(
    val listener: ScheduledTootActionListener
) : PagingDataAdapter<ScheduledStatus, BindingHolder<ItemScheduledTootBinding>>(
    object : DiffUtil.ItemCallback<ScheduledStatus>() {
        override fun areItemsTheSame(oldItem: ScheduledStatus, newItem: ScheduledStatus): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ScheduledStatus, newItem: ScheduledStatus): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemScheduledTootBinding> {
        val binding = ItemScheduledTootBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder<ItemScheduledTootBinding>, position: Int) {
        getItem(position)?.let { item ->
            holder.binding.edit.isEnabled = true
            holder.binding.delete.isEnabled = true
            holder.binding.text.text = item.params.text
            holder.binding.edit.setOnClickListener { v: View ->
                v.isEnabled = false
                listener.edit(item)
            }
            holder.binding.delete.setOnClickListener { v: View ->
                v.isEnabled = false
                listener.delete(item)
            }
        }
    }
}
