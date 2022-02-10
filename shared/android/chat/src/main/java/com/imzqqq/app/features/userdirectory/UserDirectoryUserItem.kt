package com.imzqqq.app.features.userdirectory

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.amulyakhare.textdrawable.TextDrawable
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.features.home.AvatarRenderer
import com.imzqqq.app.features.themes.ThemeUtils
import org.matrix.android.sdk.api.util.MatrixItem

@EpoxyModelClass(layout = R.layout.item_known_user)
abstract class UserDirectoryUserItem : VectorEpoxyModel<UserDirectoryUserItem.Holder>() {

    @EpoxyAttribute lateinit var avatarRenderer: AvatarRenderer
    @EpoxyAttribute lateinit var matrixItem: MatrixItem
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var clickListener: ClickListener? = null
    @EpoxyAttribute var selected: Boolean = false

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.view.onClick(clickListener)
        // If name is empty, use userId as name and force it being centered
        if (matrixItem.displayName.isNullOrEmpty()) {
            holder.userIdView.visibility = View.GONE
            holder.nameView.text = matrixItem.id
        } else {
            holder.userIdView.visibility = View.VISIBLE
            holder.nameView.text = matrixItem.displayName
            holder.userIdView.text = matrixItem.id
        }
        renderSelection(holder, selected)
    }

    private fun renderSelection(holder: Holder, isSelected: Boolean) {
        if (isSelected) {
            holder.avatarCheckedImageView.visibility = View.VISIBLE
            val backgroundColor = ThemeUtils.getColor(holder.view.context, R.attr.colorPrimary)
            val backgroundDrawable = TextDrawable.builder().buildRound("", backgroundColor)
            holder.avatarImageView.setImageDrawable(backgroundDrawable)
        } else {
            holder.avatarCheckedImageView.visibility = View.GONE
            avatarRenderer.render(matrixItem, holder.avatarImageView)
        }
    }

    class Holder : VectorEpoxyHolder() {
        val userIdView by bind<TextView>(R.id.knownUserID)
        val nameView by bind<TextView>(R.id.knownUserName)
        val avatarImageView by bind<ImageView>(R.id.knownUserAvatar)
        val avatarCheckedImageView by bind<ImageView>(R.id.knownUserAvatarChecked)
    }
}
