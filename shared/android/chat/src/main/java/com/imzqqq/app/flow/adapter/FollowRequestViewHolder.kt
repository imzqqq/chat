package com.imzqqq.app.flow.adapter

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.recyclerview.widget.RecyclerView
import com.imzqqq.app.R
import com.imzqqq.app.databinding.ItemFollowRequestBinding
import com.imzqqq.app.flow.entity.Account
import com.imzqqq.app.flow.interfaces.AccountActionListener
import com.imzqqq.app.flow.util.emojify
import com.imzqqq.app.flow.util.loadAvatar
import com.imzqqq.app.flow.util.unicodeWrap
import com.imzqqq.app.flow.util.visible

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
