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
import com.keylesspalace.flow.adapter.StatusViewHolder
import com.keylesspalace.flow.entity.Status
import com.keylesspalace.flow.interfaces.StatusActionListener
import com.keylesspalace.flow.util.StatusDisplayOptions
import com.keylesspalace.flow.viewdata.StatusViewData

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
