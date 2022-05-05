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
package com.keylesspalace.flow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.keylesspalace.flow.R
import com.keylesspalace.flow.interfaces.AccountActionListener

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
