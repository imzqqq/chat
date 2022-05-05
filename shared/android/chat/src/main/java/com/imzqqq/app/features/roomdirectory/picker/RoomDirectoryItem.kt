package com.imzqqq.app.features.roomdirectory.picker

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.core.extensions.setTextOrHide
import com.imzqqq.app.core.glide.GlideApp

@EpoxyModelClass(layout = R.layout.item_room_directory)
abstract class RoomDirectoryItem : VectorEpoxyModel<RoomDirectoryItem.Holder>() {

    @EpoxyAttribute
    var directoryAvatarUrl: String? = null

    @EpoxyAttribute
    var directoryName: String? = null

    @EpoxyAttribute
    var directoryDescription: String? = null

    @EpoxyAttribute
    var includeAllNetworks: Boolean = false

    @EpoxyAttribute
    var checked: Boolean = false

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var globalListener: ClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.rootView.onClick(globalListener)

        // Avatar
        GlideApp.with(holder.avatarView)
                .load(directoryAvatarUrl)
                .let {
                    if (!includeAllNetworks) {
                        it.placeholder(R.drawable.network_matrix)
                    } else {
                        it
                    }
                }
                .into(holder.avatarView)
        holder.avatarView.isInvisible = directoryAvatarUrl.isNullOrBlank() && includeAllNetworks

        holder.nameView.text = directoryName
        holder.descriptionView.setTextOrHide(directoryDescription)
        holder.checkedView.isVisible = checked
    }

    class Holder : VectorEpoxyHolder() {
        val rootView by bind<ViewGroup>(R.id.itemRoomDirectoryLayout)

        val avatarView by bind<ImageView>(R.id.itemRoomDirectoryAvatar)
        val nameView by bind<TextView>(R.id.itemRoomDirectoryName)
        val descriptionView by bind<TextView>(R.id.itemRoomDirectoryDescription)
        val checkedView by bind<View>(R.id.itemRoomDirectoryChecked)
    }
}
