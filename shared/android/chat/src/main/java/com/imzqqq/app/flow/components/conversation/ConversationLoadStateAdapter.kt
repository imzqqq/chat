package com.imzqqq.app.flow.components.conversation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.imzqqq.app.flow.adapter.NetworkStateViewHolder
import com.imzqqq.app.databinding.ItemNetworkStateBinding

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
