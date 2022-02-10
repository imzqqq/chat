package com.imzqqq.app.features.spaces.preview

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.extensions.setTextOrHide

@EpoxyModelClass(layout = R.layout.item_space_top_summary)
abstract class SpaceTopSummaryItem : VectorEpoxyModel<SpaceTopSummaryItem.Holder>() {

    @EpoxyAttribute
    var topic: String? = null

    @EpoxyAttribute
    lateinit var formattedMemberCount: String

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.spaceTopicText.setTextOrHide(topic)
        holder.memberCountText.text = formattedMemberCount
    }

    class Holder : VectorEpoxyHolder() {
        val memberCountText by bind<TextView>(R.id.spaceSummaryMemberCountText)
        val spaceTopicText by bind<TextView>(R.id.spaceSummaryTopic)
    }
}
