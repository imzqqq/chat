package com.imzqqq.app.features.userdirectory

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.features.home.AvatarRenderer

@EpoxyModelClass(layout = R.layout.item_invite_by_mail)
abstract class InviteByEmailItem : VectorEpoxyModel<InviteByEmailItem.Holder>() {

    @EpoxyAttribute lateinit var avatarRenderer: AvatarRenderer
    @EpoxyAttribute lateinit var foundItem: ThreePidUser
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var clickListener: ClickListener? = null
    @EpoxyAttribute var selected: Boolean = false

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.itemTitleText.text = foundItem.email
        holder.checkedImageView.isVisible = false
        holder.avatarImageView.isVisible = true
        holder.view.setOnClickListener(clickListener)
        if (selected) {
            holder.checkedImageView.isVisible = true
            holder.avatarImageView.isVisible = false
        } else {
            holder.checkedImageView.isVisible = false
            holder.avatarImageView.isVisible = true
        }
    }

    class Holder : VectorEpoxyHolder() {
        val itemTitleText by bind<TextView>(R.id.itemTitle)
        val avatarImageView by bind<ImageView>(R.id.itemAvatar)
        val checkedImageView by bind<ImageView>(R.id.itemAvatarChecked)
    }
}
