package com.imzqqq.app.features.home.room.filtered

import android.view.View
import android.widget.Button
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.features.settings.VectorPreferences

@EpoxyModelClass(layout = R.layout.item_room_filter_footer)
abstract class FilteredRoomFooterItem : VectorEpoxyModel<FilteredRoomFooterItem.Holder>() {

    @EpoxyAttribute
    var listener: Listener? = null

    @EpoxyAttribute
    var currentFilter: String = ""

    @EpoxyAttribute
    var inSpace: Boolean = false

    override fun bind(holder: Holder) {
        super.bind(holder)
        val vectorPreferences = VectorPreferences(holder.createRoomButton.context)
        holder.createRoomButton.onClick { listener?.createRoom(currentFilter) }
        holder.createDirectChat.onClick { listener?.createDirectChat() }
        holder.openRoomDirectory.onClick { listener?.openRoomDirectory(currentFilter) }
        holder.openRoomDirectory.visibility = if (vectorPreferences.simplifiedMode()) View.GONE else View.VISIBLE

        holder.openRoomDirectory.setText(
                if (inSpace) R.string.space_explore_activity_title else R.string.room_filtering_footer_open_room_directory
        )

        // The explore space screen will have a shortcut to create
        holder.createRoomButton.isVisible = !inSpace
    }

    class Holder : VectorEpoxyHolder() {
        val createRoomButton by bind<Button>(R.id.roomFilterFooterCreateRoom)
        val createDirectChat by bind<Button>(R.id.roomFilterFooterCreateDirect)
        val openRoomDirectory by bind<Button>(R.id.roomFilterFooterOpenRoomDirectory)
    }

    interface Listener {
        fun createRoom(initialName: String)
        fun createDirectChat()
        fun openRoomDirectory(initialFilter: String)
    }
}
