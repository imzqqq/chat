package com.imzqqq.app.features.home.room.detail.timeline.item

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import me.saket.bettermovementmethod.BetterLinkMovementMethod

@EpoxyModelClass(layout = R.layout.item_timeline_event_create)
abstract class RoomCreateItem : VectorEpoxyModel<RoomCreateItem.Holder>() {

    @EpoxyAttribute lateinit var text: CharSequence

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.description.movementMethod = BetterLinkMovementMethod.getInstance()
        holder.description.text = text
    }

    class Holder : VectorEpoxyHolder() {
        val description by bind<TextView>(R.id.roomCreateItemDescription)
    }
}
