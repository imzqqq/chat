package com.imzqqq.app.features.home.room.detail.timeline.item

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.features.home.AvatarRenderer

@EpoxyModelClass(layout = R.layout.item_timeline_event_base_noinfo)
abstract class MergedMembershipEventsItem : BasedMergedItem<MergedMembershipEventsItem.Holder>() {

    override fun getViewType() = STUB_ID

    @EpoxyAttribute
    override lateinit var attributes: Attributes

    override fun bind(holder: Holder) {
        super.bind(holder)
        if (attributes.isCollapsed) {
            val summary = holder.expandView.resources.getQuantityString(R.plurals.membership_changes, attributes.mergeData.size, attributes.mergeData.size)
            holder.summaryView.text = summary
            holder.summaryView.visibility = View.VISIBLE
            holder.avatarListView.visibility = View.VISIBLE
            holder.avatarListView.children.forEachIndexed { index, view ->
                val data = distinctMergeData.getOrNull(index)
                if (data != null && view is ImageView) {
                    view.visibility = View.VISIBLE
                    attributes.avatarRenderer.render(data.toMatrixItem(), view)
                } else {
                    view.visibility = View.GONE
                }
            }
        } else {
            holder.avatarListView.visibility = View.INVISIBLE
            holder.summaryView.visibility = View.GONE
        }
    }

    class Holder : BasedMergedItem.Holder(STUB_ID) {
        val summaryView by bind<TextView>(R.id.itemMergedSummaryTextView)
        val avatarListView by bind<ViewGroup>(R.id.itemMergedAvatarListView)
    }

    companion object {
        private const val STUB_ID = R.id.messageContentMergedHeaderStub
    }

    data class Attributes(
            override val isCollapsed: Boolean,
            override val mergeData: List<Data>,
            override val avatarRenderer: AvatarRenderer,
            override val onCollapsedStateChanged: (Boolean) -> Unit
    ) : BasedMergedItem.Attributes
}
