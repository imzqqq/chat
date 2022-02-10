package com.imzqqq.app.flow.components.drafts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imzqqq.app.databinding.ItemDraftBinding
import com.imzqqq.app.flow.db.DraftEntity
import com.imzqqq.app.flow.util.BindingHolder
import com.imzqqq.app.flow.util.hide
import com.imzqqq.app.flow.util.show
import com.imzqqq.app.flow.util.visible

interface DraftActionListener {
    fun onOpenDraft(draft: DraftEntity)
    fun onDeleteDraft(draft: DraftEntity)
}

class DraftsAdapter(
    private val listener: DraftActionListener
) : PagingDataAdapter<DraftEntity, BindingHolder<ItemDraftBinding>>(
    object : DiffUtil.ItemCallback<DraftEntity>() {
        override fun areItemsTheSame(oldItem: DraftEntity, newItem: DraftEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DraftEntity, newItem: DraftEntity): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemDraftBinding> {

        val binding = ItemDraftBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val viewHolder = BindingHolder(binding)

        binding.draftMediaPreview.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
        binding.draftMediaPreview.adapter = DraftMediaAdapter {
            getItem(viewHolder.bindingAdapterPosition)?.let { draft ->
                listener.onOpenDraft(draft)
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: BindingHolder<ItemDraftBinding>, position: Int) {
        getItem(position)?.let { draft ->
            holder.binding.root.setOnClickListener {
                listener.onOpenDraft(draft)
            }
            holder.binding.deleteButton.setOnClickListener {
                listener.onDeleteDraft(draft)
            }
            holder.binding.draftSendingInfo.visible(draft.failedToSend)

            holder.binding.contentWarning.visible(!draft.contentWarning.isNullOrEmpty())
            holder.binding.contentWarning.text = draft.contentWarning
            holder.binding.content.text = draft.content

            holder.binding.draftMediaPreview.visible(draft.attachments.isNotEmpty())
            (holder.binding.draftMediaPreview.adapter as DraftMediaAdapter).submitList(draft.attachments)

            if (draft.poll != null) {
                holder.binding.draftPoll.show()
                holder.binding.draftPoll.setPoll(draft.poll)
            } else {
                holder.binding.draftPoll.hide()
            }
        }
    }
}
