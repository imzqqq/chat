package com.imzqqq.app.features.home.room.detail.search

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.core.extensions.setTextOrHide
import com.imzqqq.app.features.displayname.getBestName
import com.imzqqq.app.features.home.AvatarRenderer
import org.matrix.android.sdk.api.util.MatrixItem

@EpoxyModelClass(layout = R.layout.item_search_result)
abstract class SearchResultItem : VectorEpoxyModel<SearchResultItem.Holder>() {

    @EpoxyAttribute lateinit var avatarRenderer: AvatarRenderer
    @EpoxyAttribute var formattedDate: String? = null
    @EpoxyAttribute lateinit var spannable: CharSequence
    @EpoxyAttribute var sender: MatrixItem? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var listener: ClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)

        holder.view.onClick(listener)
        sender?.let { avatarRenderer.render(it, holder.avatarImageView) }
        holder.memberNameView.setTextOrHide(sender?.getBestName())
        holder.timeView.text = formattedDate
        holder.contentView.text = spannable
    }

    class Holder : VectorEpoxyHolder() {
        val avatarImageView by bind<ImageView>(R.id.messageAvatarImageView)
        val memberNameView by bind<TextView>(R.id.messageMemberNameView)
        val timeView by bind<TextView>(R.id.messageTimeView)
        val contentView by bind<TextView>(R.id.messageContentView)
    }
}
