package com.imzqqq.app.flow.components.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.imzqqq.app.databinding.ItemHashtagBinding
import com.imzqqq.app.flow.entity.HashTag
import com.imzqqq.app.flow.interfaces.LinkListener
import com.imzqqq.app.flow.util.BindingHolder

class SearchHashtagsAdapter(private val linkListener: LinkListener) :
    PagingDataAdapter<HashTag, BindingHolder<ItemHashtagBinding>>(HASHTAG_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemHashtagBinding> {
        val binding = ItemHashtagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder<ItemHashtagBinding>, position: Int) {
        getItem(position)?.let { (name) ->
            holder.binding.root.text = String.format("#%s", name)
            holder.binding.root.setOnClickListener { linkListener.onViewTag(name) }
        }
    }

    companion object {

        val HASHTAG_COMPARATOR = object : DiffUtil.ItemCallback<HashTag>() {
            override fun areContentsTheSame(oldItem: HashTag, newItem: HashTag): Boolean =
                oldItem.name == newItem.name

            override fun areItemsTheSame(oldItem: HashTag, newItem: HashTag): Boolean =
                oldItem.name == newItem.name
        }
    }
}
