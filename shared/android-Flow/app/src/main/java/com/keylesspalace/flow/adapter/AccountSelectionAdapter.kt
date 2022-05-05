/* Copyright 2019 Levi Bard
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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.preference.PreferenceManager
import com.keylesspalace.flow.R
import com.keylesspalace.flow.databinding.ItemAutocompleteAccountBinding
import com.keylesspalace.flow.db.AccountEntity
import com.keylesspalace.flow.settings.PrefKeys
import com.keylesspalace.flow.util.emojify
import com.keylesspalace.flow.util.loadAvatar

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
