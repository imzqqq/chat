package com.imzqqq.app.features.home.room.detail.timeline.item

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_timeline_event_day_separator)
abstract class DaySeparatorItem : EpoxyModelWithHolder<DaySeparatorItem.Holder>() {

    @EpoxyAttribute lateinit var formattedDay: CharSequence

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.dayTextView.text = formattedDay
    }

    class Holder : VectorEpoxyHolder() {
        val dayTextView by bind<TextView>(R.id.itemDayTextView)
    }
}
