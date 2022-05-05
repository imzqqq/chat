package com.imzqqq.app.flow.components.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.imzqqq.app.R
import com.imzqqq.app.flow.adapter.AccountViewHolder
import com.imzqqq.app.flow.entity.Account
import com.imzqqq.app.flow.interfaces.LinkListener

class SearchAccountsAdapter(private val linkListener: LinkListener, private val animateAvatars: Boolean, private val animateEmojis: Boolean) :
    PagingDataAdapter<Account, AccountViewHolder>(ACCOUNT_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_account, parent, false)
        return AccountViewHolder(view)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.apply {
                setupWithAccount(item, animateAvatars, animateEmojis)
                setupLinkListener(linkListener)
            }
        }
    }

    companion object {

        val ACCOUNT_COMPARATOR = object : DiffUtil.ItemCallback<Account>() {
            override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean =
                oldItem.deepEquals(newItem)

            override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean =
                oldItem.id == newItem.id
        }
    }
}
