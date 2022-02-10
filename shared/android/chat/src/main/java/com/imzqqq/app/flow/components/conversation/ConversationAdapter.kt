package com.imzqqq.app.flow.components.conversation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.imzqqq.app.R
import com.imzqqq.app.flow.interfaces.StatusActionListener
import com.imzqqq.app.flow.util.StatusDisplayOptions

class ConversationAdapter(
    private val statusDisplayOptions: StatusDisplayOptions,
    private val listener: StatusActionListener
) : PagingDataAdapter<ConversationEntity, ConversationViewHolder>(CONVERSATION_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_conversation, parent, false)
        return ConversationViewHolder(view, statusDisplayOptions, listener)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.setupWithConversation(getItem(position))
    }

    fun item(position: Int): ConversationEntity? {
        return getItem(position)
    }

    companion object {
        val CONVERSATION_COMPARATOR = object : DiffUtil.ItemCallback<ConversationEntity>() {
            override fun areItemsTheSame(oldItem: ConversationEntity, newItem: ConversationEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ConversationEntity, newItem: ConversationEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
