package com.imzqqq.app.features.home.room.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.databinding.ItemRoomCategoryBinding
import com.imzqqq.app.features.themes.ThemeUtils

class SectionHeaderAdapter constructor(
        private val onClickAction: ClickListener
) : RecyclerView.Adapter<SectionHeaderAdapter.VH>() {

    data class RoomsSectionData(
            val name: String,
            val isExpanded: Boolean = true,
            val notificationCount: Int = 0,
            val isHighlighted: Boolean = false,
            val isHidden: Boolean = true,
            // SC additions
            val unread: Int = 0,
            val markedUnread: Boolean = false,

            // This will be false until real data has been submitted once
            val isLoading: Boolean = true
    )

    lateinit var roomsSectionData: RoomsSectionData
        private set

    fun updateSection(newRoomsSectionData: RoomsSectionData) {
        if (!::roomsSectionData.isInitialized || newRoomsSectionData != roomsSectionData) {
            roomsSectionData = newRoomsSectionData
            notifyDataSetChanged()
        }
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = roomsSectionData.hashCode().toLong()

    override fun getItemViewType(position: Int) = R.layout.item_room_category

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH.create(parent, onClickAction)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(roomsSectionData)
    }

    override fun getItemCount(): Int = if (roomsSectionData.isHidden) 0 else 1

    class VH constructor(
            private val binding: ItemRoomCategoryBinding,
            onClickAction: ClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.onClick(onClickAction)
        }

        fun bind(roomsSectionData: RoomsSectionData) {
            binding.roomCategoryTitleView.text = roomsSectionData.name
            val tintColor = ThemeUtils.getColor(binding.root.context, R.attr.vctr_content_secondary)
            val expandedArrowDrawableRes = if (roomsSectionData.isExpanded) R.drawable.ic_expand_more else R.drawable.ic_expand_less
            val expandedArrowDrawable = ContextCompat.getDrawable(binding.root.context, expandedArrowDrawableRes)?.also {
                DrawableCompat.setTint(it, tintColor)
            }
            binding.roomCategoryUnreadCounterBadgeView.render(UnreadCounterBadgeView.State(roomsSectionData.notificationCount, roomsSectionData.isHighlighted, roomsSectionData.unread, roomsSectionData.markedUnread))
            binding.roomCategoryTitleView.setCompoundDrawablesWithIntrinsicBounds(null, null, expandedArrowDrawable, null)
        }

        companion object {
            fun create(parent: ViewGroup, onClickAction: ClickListener): VH {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_room_category, parent, false)
                val binding = ItemRoomCategoryBinding.bind(view)
                return VH(binding, onClickAction)
            }
        }
    }
}
