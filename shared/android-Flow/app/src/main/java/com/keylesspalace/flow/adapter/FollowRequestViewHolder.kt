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

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.recyclerview.widget.RecyclerView
import com.keylesspalace.flow.R
import com.keylesspalace.flow.databinding.ItemFollowRequestBinding
import com.keylesspalace.flow.entity.Account
import com.keylesspalace.flow.interfaces.AccountActionListener
import com.keylesspalace.flow.util.emojify
import com.keylesspalace.flow.util.loadAvatar
import com.keylesspalace.flow.util.unicodeWrap
import com.keylesspalace.flow.util.visible

class FollowRequestViewHolder(
    private val binding: ItemFollowRequestBinding,
    private val showHeader: Boolean
) : RecyclerView.ViewHolder(binding.root) {

    fun setupWithAccount(account: Account, animateAvatar: Boolean, animateEmojis: Boolean) {
        val wrappedName = account.name.unicodeWrap()
        val emojifiedName: CharSequence = wrappedName.emojify(account.emojis, itemView, animateEmojis)
        binding.displayNameTextView.text = emojifiedName
        if (showHeader) {
            val wholeMessage: String = itemView.context.getString(R.string.notification_follow_request_format, wrappedName)
            binding.notificationTextView.text = SpannableStringBuilder(wholeMessage).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, wrappedName.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }.emojify(account.emojis, itemView, animateEmojis)
        }
        binding.notificationTextView.visible(showHeader)
        val format = itemView.context.getString(R.string.status_username_format)
        val formattedUsername = String.format(format, account.username)
        binding.usernameTextView.text = formattedUsername
        val avatarRadius = binding.avatar.context.resources.getDimensionPixelSize(R.dimen.avatar_radius_48dp)
        loadAvatar(account.avatar, binding.avatar, avatarRadius, animateAvatar)
    }

    fun setupActionListener(listener: AccountActionListener, accountId: String) {
        binding.acceptButton.setOnClickListener {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onRespondToFollowRequest(true, accountId, position)
            }
        }
        binding.rejectButton.setOnClickListener {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onRespondToFollowRequest(false, accountId, position)
            }
        }
        itemView.setOnClickListener { listener.onViewAccount(accountId) }
    }
}
