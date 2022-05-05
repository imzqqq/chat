package com.imzqqq.app.features.home.room.detail.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.imzqqq.app.R
import com.imzqqq.app.databinding.ViewRoomWidgetsBannerBinding
import org.matrix.android.sdk.api.session.widgets.model.Widget

class RoomWidgetsBannerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    interface Callback {
        fun onViewWidgetsClicked()
    }

    private val views: ViewRoomWidgetsBannerBinding

    var callback: Callback? = null

    init {
        setupView()
        views = ViewRoomWidgetsBannerBinding.bind(this)
    }

    private fun setupView() {
        inflate(context, R.layout.view_room_widgets_banner, this)
        setBackgroundResource(R.drawable.bg_active_widgets_banner)
        setOnClickListener {
            callback?.onViewWidgetsClicked()
        }
    }

    fun render(widgets: List<Widget>?) {
        if (widgets.isNullOrEmpty()) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
            views.activeWidgetsLabel.text = context.resources.getQuantityString(R.plurals.active_widgets, widgets.size, widgets.size)
        }
    }
}
