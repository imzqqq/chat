package com.imzqqq.app.features.spaces.manage

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.features.displayname.getBestName
import com.imzqqq.app.features.home.AvatarRenderer
import org.matrix.android.sdk.api.util.MatrixItem

@EpoxyModelClass(layout = R.layout.item_room_to_manage_in_space)
abstract class RoomManageSelectionItem : VectorEpoxyModel<RoomManageSelectionItem.Holder>() {

    @EpoxyAttribute lateinit var avatarRenderer: AvatarRenderer
    @EpoxyAttribute lateinit var matrixItem: MatrixItem
    @EpoxyAttribute var selected: Boolean = false
    @EpoxyAttribute var suggested: Boolean = false
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var itemClickListener: ClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        avatarRenderer.render(matrixItem, holder.avatarImageView)

        holder.titleText.text = matrixItem.getBestName()

        if (selected) {
            holder.checkboxImage.setImageDrawable(ContextCompat.getDrawable(holder.view.context, R.drawable.ic_checkbox_on))
            holder.checkboxImage.contentDescription = holder.view.context.getString(R.string.a11y_checked)
        } else {
            holder.checkboxImage.setImageDrawable(ContextCompat.getDrawable(holder.view.context, R.drawable.ic_checkbox_off))
            holder.checkboxImage.contentDescription = holder.view.context.getString(R.string.a11y_unchecked)
        }

        holder.suggestedText.isVisible = suggested

        holder.view.onClick(itemClickListener)
    }

    class Holder : VectorEpoxyHolder() {
        val avatarImageView by bind<ImageView>(R.id.itemAddRoomRoomAvatar)
        val titleText by bind<TextView>(R.id.itemAddRoomRoomNameText)
        val suggestedText by bind<TextView>(R.id.itemManageRoomSuggested)
        val checkboxImage by bind<ImageView>(R.id.itemAddRoomRoomCheckBox)
    }
}
