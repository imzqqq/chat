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

package com.keylesspalace.flow.components.conversation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.keylesspalace.flow.R
import com.keylesspalace.flow.interfaces.StatusActionListener
import com.keylesspalace.flow.util.StatusDisplayOptions

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
