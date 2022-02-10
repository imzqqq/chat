package com.imzqqq.app.flow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imzqqq.app.databinding.ItemEmojiButtonBinding
import com.imzqqq.app.flow.entity.Emoji
import com.imzqqq.app.flow.util.BindingHolder
import java.util.Locale

class EmojiAdapter(
        emojiList: List<Emoji>,
        private val onEmojiSelectedListener: OnEmojiSelectedListener
) : RecyclerView.Adapter<BindingHolder<ItemEmojiButtonBinding>>() {

    private val emojiList: List<Emoji> = emojiList.filter { emoji -> emoji.visibleInPicker == null || emoji.visibleInPicker }
        .sortedBy { it.shortcode.lowercase(Locale.ROOT) }

    override fun getItemCount() = emojiList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemEmojiButtonBinding> {
        val binding = ItemEmojiButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder<ItemEmojiButtonBinding>, position: Int) {
        val emoji = emojiList[position]
        val emojiImageView = holder.binding.root

        Glide.with(emojiImageView)
            .load(emoji.url)
            .into(emojiImageView)

        emojiImageView.setOnClickListener {
            onEmojiSelectedListener.onEmojiSelected(emoji.shortcode)
        }

        emojiImageView.contentDescription = emoji.shortcode
    }
}

interface OnEmojiSelectedListener {
    fun onEmojiSelected(shortcode: String)
}
