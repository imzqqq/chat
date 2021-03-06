/* Copyright 2021 Flow Contributors
 *
 * This file is a part of Flow.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Flow is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Flow; if not,
 * see <http://www.gnu.org/licenses>. */

package com.keylesspalace.flow.components.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.keylesspalace.flow.R
import com.keylesspalace.flow.adapter.AccountViewHolder
import com.keylesspalace.flow.entity.Account
import com.keylesspalace.flow.interfaces.LinkListener

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
