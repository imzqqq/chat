package com.imzqqq.app.features.home.room.list

import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.features.themes.ThemeUtils

@EpoxyModelClass(layout = R.layout.item_room_category)
abstract class RoomCategoryItem : VectorEpoxyModel<RoomCategoryItem.Holder>() {

    @EpoxyAttribute lateinit var title: CharSequence
    @EpoxyAttribute var expanded: Boolean = false
    @EpoxyAttribute var unreadNotificationCount: Int = 0
    @EpoxyAttribute var unreadMessages: Int = 0
    @EpoxyAttribute var showHighlighted: Boolean = false
    @EpoxyAttribute var markedUnread: Boolean = false
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var listener: ClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        val tintColor = ThemeUtils.getColor(holder.rootView.context, R.attr.vctr_content_secondary)
        val expandedArrowDrawableRes = if (expanded) R.drawable.ic_expand_more else R.drawable.ic_expand_less
        val expandedArrowDrawable = ContextCompat.getDrawable(holder.rootView.context, expandedArrowDrawableRes)?.also {
            DrawableCompat.setTint(it, tintColor)
        }
        holder.unreadCounterBadgeView.render(UnreadCounterBadgeView.State(unreadNotificationCount, showHighlighted, unreadMessages, markedUnread))
        holder.titleView.setCompoundDrawablesWithIntrinsicBounds(null, null, expandedArrowDrawable, null)
        holder.titleView.text = title
        holder.rootView.onClick(listener)
    }

    class Holder : VectorEpoxyHolder() {
        val unreadCounterBadgeView by bind<UnreadCounterBadgeView>(R.id.roomCategoryUnreadCounterBadgeView)
        val titleView by bind<TextView>(R.id.roomCategoryTitleView)
        val rootView by bind<ViewGroup>(R.id.roomCategoryRootView)
    }
}
