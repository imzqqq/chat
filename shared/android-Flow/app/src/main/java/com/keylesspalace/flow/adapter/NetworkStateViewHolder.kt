/* Copyright 2019 Conny Duck
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

package com.keylesspalace.flow.adapter

import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.keylesspalace.flow.databinding.ItemNetworkStateBinding
import com.keylesspalace.flow.util.visible

class NetworkStateViewHolder(
    private val binding: ItemNetworkStateBinding,
    private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun setUpWithNetworkState(state: LoadState) {
        binding.progressBar.visible(state == LoadState.Loading)
        binding.retryButton.visible(state is LoadState.Error)
        val msg = if (state is LoadState.Error) {
            state.error.message
        } else {
            null
        }
        binding.errorMsg.visible(msg != null)
        binding.errorMsg.text = msg
        binding.retryButton.setOnClickListener {
            retryCallback()
        }
    }
}
