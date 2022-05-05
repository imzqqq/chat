package com.imzqqq.app.features.roomdirectory

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.features.home.AvatarRenderer
import org.matrix.android.sdk.api.util.MatrixItem

@EpoxyModelClass(layout = R.layout.item_unknown_room)
abstract class UnknownRoomItem : VectorEpoxyModel<UnknownRoomItem.Holder>() {

    @EpoxyAttribute
    lateinit var avatarRenderer: AvatarRenderer

    @EpoxyAttribute
    lateinit var matrixItem: MatrixItem

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var globalListener: ClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.rootView.onClick(globalListener)
        avatarRenderer.render(matrixItem, holder.avatarView)
        holder.nameView.text = matrixItem.displayName
    }

    class Holder : VectorEpoxyHolder() {
        val rootView by bind<ViewGroup>(R.id.itemUnknownRoomLayout)
        val avatarView by bind<ImageView>(R.id.itemUnknownRoomAvatar)
        val nameView by bind<TextView>(R.id.itemUnknownRoomName)
    }
}
