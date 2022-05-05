package com.imzqqq.app.flow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.imzqqq.app.R
import com.imzqqq.app.flow.interfaces.AccountActionListener

/** Displays either a follows or following list.  */
class FollowAdapter(
        accountActionListener: AccountActionListener,
        animateAvatar: Boolean,
        animateEmojis: Boolean
) : AccountAdapter<AccountViewHolder>(accountActionListener, animateAvatar, animateEmojis) {
    override fun createAccountViewHolder(parent: ViewGroup): AccountViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_account, parent, false)
        return AccountViewHolder(view)
    }

    override fun onBindAccountViewHolder(viewHolder: AccountViewHolder, position: Int) {
        viewHolder.setupWithAccount(accountList[position], animateAvatar, animateEmojis)
        viewHolder.setupActionListener(accountActionListener)
    }
}
