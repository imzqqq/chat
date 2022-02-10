package com.imzqqq.app.features.home.room.detail.widget

import com.airbnb.epoxy.TypedEpoxyController
import com.imzqqq.app.R
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.list.genericButtonItem
import com.imzqqq.app.core.ui.list.genericFooterItem
import org.matrix.android.sdk.api.session.widgets.model.Widget
import javax.inject.Inject

/**
 * Epoxy controller for room widgets list
 */
class RoomWidgetsController @Inject constructor(
        val stringProvider: StringProvider,
        val colorProvider: ColorProvider) :
    TypedEpoxyController<List<Widget>>() {

    var listener: Listener? = null

    override fun buildModels(widgets: List<Widget>) {
        val host = this
        if (widgets.isEmpty()) {
            genericFooterItem {
                id("empty")
                text(host.stringProvider.getString(R.string.room_no_active_widgets))
            }
        } else {
            widgets.forEach { widget ->
                roomWidgetItem {
                    id(widget.widgetId)
                    widget(widget)
                    widgetClicked { host.listener?.didSelectWidget(widget) }
                }
            }
        }
        genericButtonItem {
            id("addIntegration")
            text(host.stringProvider.getString(R.string.room_manage_integrations))
            textColor(host.colorProvider.getColorFromAttribute(R.attr.colorPrimary))
            buttonClickAction { host.listener?.didSelectManageWidgets() }
        }
    }

    interface Listener {
        fun didSelectWidget(widget: Widget)
        fun didSelectManageWidgets()
    }
}
