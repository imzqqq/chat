package com.imzqqq.app.core.epoxy

import android.animation.ObjectAnimator
import android.text.TextUtils
import android.text.method.MovementMethod
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.copyOnLongClick

@EpoxyModelClass(layout = R.layout.item_expandable_textview)
abstract class ExpandableTextItem : VectorEpoxyModel<ExpandableTextItem.Holder>() {

    @EpoxyAttribute
    lateinit var content: String

    @EpoxyAttribute
    var maxLines: Int = 3

    // Number of hidden lines that justify collapsing
    @EpoxyAttribute
    var minCollapsedLines: Int = 2

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var movementMethod: MovementMethod? = null

    private var isExpanded = false
    private var expandedLines = 0

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.content.text = content
        holder.content.copyOnLongClick()
        holder.content.movementMethod = movementMethod

        holder.content.doOnPreDraw {
            // Allow for displaying maxLines + 1 lines: in this case, the expand button
            // would reserve just as much space as the expaned text
            if (holder.content.lineCount > maxLines + minCollapsedLines) {
                expandedLines = holder.content.lineCount
                holder.content.maxLines = maxLines

                holder.view.setOnClickListener {
                    if (isExpanded) {
                        collapse(holder)
                    } else {
                        expand(holder)
                    }
                }
                holder.arrow.isVisible = true
            } else {
                holder.arrow.isVisible = false
                holder.content.maxLines = maxLines + minCollapsedLines
            }
        }
    }

    private fun expand(holder: Holder) {
        ObjectAnimator
                .ofInt(holder.content, "maxLines", expandedLines)
                .setDuration(200)
                .start()

        holder.content.ellipsize = null
        holder.arrow.setImageResource(R.drawable.ic_expand_less)
        holder.arrow.contentDescription = holder.view.context.getString(R.string.merged_events_collapse)
        isExpanded = true
    }

    private fun collapse(holder: Holder) {
        ObjectAnimator
                .ofInt(holder.content, "maxLines", maxLines)
                .setDuration(200)
                .start()

        holder.content.ellipsize = TextUtils.TruncateAt.END
        holder.arrow.setImageResource(R.drawable.ic_expand_more)
        holder.arrow.contentDescription = holder.view.context.getString(R.string.merged_events_expand)
        isExpanded = false
    }

    class Holder : VectorEpoxyHolder() {
        val content by bind<TextView>(R.id.expandableContent)
        val arrow by bind<ImageView>(R.id.expandableArrow)
    }
}
