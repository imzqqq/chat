package com.imzqqq.app.flow.components.drafts

import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.imzqqq.app.R
import com.imzqqq.app.flow.db.DraftAttachment

class DraftMediaAdapter(
    private val attachmentClick: () -> Unit
) : ListAdapter<DraftAttachment, DraftMediaAdapter.DraftMediaViewHolder>(
    object : DiffUtil.ItemCallback<DraftAttachment>() {
        override fun areItemsTheSame(oldItem: DraftAttachment, newItem: DraftAttachment): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DraftAttachment, newItem: DraftAttachment): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftMediaViewHolder {
        return DraftMediaViewHolder(AppCompatImageView(parent.context))
    }

    override fun onBindViewHolder(holder: DraftMediaViewHolder, position: Int) {
        getItem(position)?.let { attachment ->
            if (attachment.type == DraftAttachment.Type.AUDIO) {
                holder.imageView.setImageResource(R.drawable.ic_music_box_preview_24dp)
            } else {
                Glide.with(holder.itemView.context)
                    .load(attachment.uri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .dontAnimate()
                    .into(holder.imageView)
            }
        }
    }

    inner class DraftMediaViewHolder(val imageView: ImageView) :
        RecyclerView.ViewHolder(imageView) {
        init {
            val thumbnailViewSize =
                imageView.context.resources.getDimensionPixelSize(R.dimen.compose_media_preview_size)
            val layoutParams = ConstraintLayout.LayoutParams(thumbnailViewSize, thumbnailViewSize)
            val margin = itemView.context.resources
                .getDimensionPixelSize(R.dimen.compose_media_preview_margin)
            val marginBottom = itemView.context.resources
                .getDimensionPixelSize(R.dimen.compose_media_preview_margin_bottom)
            layoutParams.setMargins(margin, 0, margin, marginBottom)
            imageView.layoutParams = layoutParams
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setOnClickListener {
                attachmentClick()
            }
        }
    }
}
