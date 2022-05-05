package com.imzqqq.app.flow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.imzqqq.app.databinding.ItemFollowRequestBinding
import com.imzqqq.app.flow.interfaces.AccountActionListener

/** Displays a list of follow requests with accept/reject buttons. */
class FollowRequestsAdapter(
        accountActionListener: AccountActionListener,
        animateAvatar: Boolean,
        animateEmojis: Boolean
) : AccountAdapter<FollowRequestViewHolder>(accountActionListener, animateAvatar, animateEmojis) {
    override fun createAccountViewHolder(parent: ViewGroup): FollowRequestViewHolder {
        val binding = ItemFollowRequestBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FollowRequestViewHolder(binding, false)
    }

    override fun onBindAccountViewHolder(viewHolder: FollowRequestViewHolder, position: Int) {
        viewHolder.setupWithAccount(accountList[position], animateAvatar, animateEmojis)
        viewHolder.setupActionListener(accountActionListener, accountList[position].id)
    }
}
