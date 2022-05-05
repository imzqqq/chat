package com.imzqqq.app.flow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.preference.PreferenceManager
import com.imzqqq.app.R
import com.imzqqq.app.databinding.ItemAutocompleteAccountBinding
import com.imzqqq.app.flow.db.AccountEntity
import com.imzqqq.app.flow.settings.PrefKeys
import com.imzqqq.app.flow.util.emojify
import com.imzqqq.app.flow.util.loadAvatar

class AccountSelectionAdapter(context: Context) : ArrayAdapter<AccountEntity>(context, R.layout.item_autocomplete_account) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = if (convertView == null) {
            ItemAutocompleteAccountBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            ItemAutocompleteAccountBinding.bind(convertView)
        }

        val account = getItem(position)
        if (account != null) {
            val pm = PreferenceManager.getDefaultSharedPreferences(binding.avatar.context)
            val animateEmojis = pm.getBoolean(PrefKeys.ANIMATE_CUSTOM_EMOJIS, false)

            binding.username.text = account.fullName
            binding.displayName.text = account.displayName.emojify(account.emojis, binding.displayName, animateEmojis)

            val avatarRadius = context.resources.getDimensionPixelSize(R.dimen.avatar_radius_42dp)
            val animateAvatar = pm.getBoolean("animateGifAvatars", false)

            loadAvatar(account.profilePictureUrl, binding.avatar, avatarRadius, animateAvatar)
        }

        return binding.root
    }
}
