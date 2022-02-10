package com.imzqqq.app.flow.components.announcements

import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.imzqqq.app.R
import com.imzqqq.app.databinding.ItemAnnouncementBinding
import com.imzqqq.app.flow.entity.Announcement
import com.imzqqq.app.flow.entity.Emoji
import com.imzqqq.app.flow.interfaces.LinkListener
import com.imzqqq.app.flow.util.BindingHolder
import com.imzqqq.app.flow.util.LinkHelper
import com.imzqqq.app.flow.util.emojify

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
