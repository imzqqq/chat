package com.imzqqq.app.flow.adapter

import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.imzqqq.app.databinding.ItemNetworkStateBinding
import com.imzqqq.app.flow.util.visible

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
