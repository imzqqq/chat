/* Copyright 2020 Flow Contributors
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

package com.keylesspalace.flow.components.announcements

import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.keylesspalace.flow.R
import com.keylesspalace.flow.databinding.ItemAnnouncementBinding
import com.keylesspalace.flow.entity.Announcement
import com.keylesspalace.flow.entity.Emoji
import com.keylesspalace.flow.interfaces.LinkListener
import com.keylesspalace.flow.util.BindingHolder
import com.keylesspalace.flow.util.LinkHelper
import com.keylesspalace.flow.util.emojify

interface AnnouncementActionListener : LinkListener {
    fun openReactionPicker(announcementId: String, target: View)
    fun addReaction(announcementId: String, name: String)
    fun removeReaction(announcementId: String, name: String)
}

class AnnouncementAdapter(
    private var items: List<Announcement> = emptyList(),
    private val listener: AnnouncementActionListener,
    private val wellbeingEnabled: Boolean = false,
    private val animateEmojis: Boolean = false
) : RecyclerView.Adapter<BindingHolder<ItemAnnouncementBinding>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemAnnouncementBinding> {
        val binding = ItemAnnouncementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder<ItemAnnouncementBinding>, position: Int) {
        val item = items[position]

        val text = holder.binding.text
        val chips = holder.binding.chipGroup
        val addReactionChip = holder.binding.addReactionChip

        LinkHelper.setClickableText(text, item.content, null, listener)

        // If wellbeing mode is enabled, announcement badge counts should not be shown.
        if (wellbeingEnabled) {
            // Since reactions are not visible in wellbeing mode,
            // we shouldn't be able to add any ourselves.
            addReactionChip.visibility = View.GONE
            return
        }

        item.reactions.forEachIndexed { i, reaction ->
            chips.getChildAt(i)?.takeUnless { it.id == R.id.addReactionChip } as Chip?
                ?: Chip(ContextThemeWrapper(chips.context, R.style.Widget_MaterialComponents_Chip_Choice)).apply {
                    isCheckable = true
                    checkedIcon = null
                    chips.addView(this, i)
                }
                    .apply {
                        val emojiText = if (reaction.url == null) {
                            reaction.name
                        } else {
                            context.getString(R.string.emoji_shortcode_format, reaction.name)
                        }
                        this.text = ("$emojiText ${reaction.count}")
                            .emojify(
                                listOf(
                                    Emoji(
                                        reaction.name,
                                        reaction.url ?: "",
                                        reaction.staticUrl ?: "",
                                        null
                                    )
                                ),
                                this,
                                animateEmojis
                            )

                        isChecked = reaction.me

                        setOnClickListener {
                            if (reaction.me) {
                                listener.removeReaction(item.id, reaction.name)
                            } else {
                                listener.addReaction(item.id, reaction.name)
                            }
                        }
                    }
        }

        while (chips.size - 1 > item.reactions.size) {
            chips.removeViewAt(item.reactions.size)
        }

        addReactionChip.setOnClickListener {
            listener.openReactionPicker(item.id, it)
        }
    }

    override fun getItemCount() = items.size

    fun updateList(items: List<Announcement>) {
        this.items = items
        notifyDataSetChanged()
    }
}
