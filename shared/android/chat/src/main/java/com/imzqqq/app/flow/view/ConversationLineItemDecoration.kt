package com.imzqqq.app.flow.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.imzqqq.app.R
import com.imzqqq.app.flow.adapter.ThreadAdapter

class ConversationLineItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {

    private val divider: Drawable = ContextCompat.getDrawable(context, R.drawable.conversation_thread_line)!!

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerStart = parent.paddingStart + context.resources.getDimensionPixelSize(R.dimen.status_line_margin_start)
        val dividerEnd = dividerStart + divider.intrinsicWidth

        val childCount = parent.childCount
        val avatarMargin = context.resources.getDimensionPixelSize(R.dimen.account_avatar_margin)

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val position = parent.getChildAdapterPosition(child)
            val adapter = parent.adapter as ThreadAdapter

            val current = adapter.getItem(position)
            val dividerTop: Int
            val dividerBottom: Int
            if (current != null) {
                val above = adapter.getItem(position - 1)
                dividerTop = if (above != null && above.id == current.status.inReplyToId) {
                    child.top
                } else {
                    child.top + avatarMargin
                }
                val below = adapter.getItem(position + 1)
                dividerBottom = if (below != null && current.id == below.status.inReplyToId &&
                    adapter.detailedStatusPosition != position
                ) {
                    child.bottom
                } else {
                    child.top + avatarMargin
                }

                if (parent.layoutDirection == View.LAYOUT_DIRECTION_LTR) {
                    divider.setBounds(dividerStart, dividerTop, dividerEnd, dividerBottom)
                } else {
                    divider.setBounds(canvas.width - dividerEnd, dividerTop, canvas.width - dividerStart, dividerBottom)
                }
                divider.draw(canvas)
            }
        }
    }
}
