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

package com.keylesspalace.flow.components.conversation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.keylesspalace.flow.adapter.NetworkStateViewHolder
import com.keylesspalace.flow.databinding.ItemNetworkStateBinding

class ConversationLoadStateAdapter(
    private val retryCallback: () -> Unit
) : LoadStateAdapter<NetworkStateViewHolder>() {

    override fun onBindViewHolder(holder: NetworkStateViewHolder, loadState: LoadState) {
        holder.setUpWithNetworkState(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NetworkStateViewHolder {
        val binding = ItemNetworkStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NetworkStateViewHolder(binding, retryCallback)
    }
}
