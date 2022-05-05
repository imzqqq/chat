package com.imzqqq.app.features.crypto.verification.cancel

import com.airbnb.epoxy.EpoxyController
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.bottomSheetDividerItem
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.features.crypto.verification.VerificationBottomSheetViewState
import com.imzqqq.app.features.crypto.verification.epoxy.bottomSheetVerificationActionItem
import com.imzqqq.app.features.crypto.verification.epoxy.bottomSheetVerificationNoticeItem
import com.imzqqq.app.features.html.EventHtmlRenderer
import javax.inject.Inject

class VerificationNotMeController @Inject constructor(
        private val stringProvider: StringProvider,
        private val colorProvider: ColorProvider,
        private val eventHtmlRenderer: EventHtmlRenderer
) : EpoxyController() {

    var listener: Listener? = null

    private var viewState: VerificationBottomSheetViewState? = null

    fun update(viewState: VerificationBottomSheetViewState) {
        this.viewState = viewState
        requestModelBuild()
    }

    override fun buildModels() {
        val host = this
        bottomSheetVerificationNoticeItem {
            id("notice")
            notice(host.eventHtmlRenderer.render(host.stringProvider.getString(R.string.verify_not_me_self_verification)))
        }

        bottomSheetDividerItem {
            id("sep0")
        }

        bottomSheetVerificationActionItem {
            id("skip")
            title(host.stringProvider.getString(R.string.skip))
            titleColor(host.colorProvider.getColorFromAttribute(R.attr.vctr_content_primary))
            iconRes(R.drawable.ic_arrow_right)
            iconColor(host.colorProvider.getColorFromAttribute(R.attr.vctr_content_primary))
            listener { host.listener?.onTapSkip() }
        }

        bottomSheetDividerItem {
            id("sep1")
        }

        bottomSheetVerificationActionItem {
            id("settings")
            title(host.stringProvider.getString(R.string.settings))
            titleColor(host.colorProvider.getColorFromAttribute(R.attr.colorPrimary))
            iconRes(R.drawable.ic_arrow_right)
            iconColor(host.colorProvider.getColorFromAttribute(R.attr.colorPrimary))
            listener { host.listener?.onTapSettings() }
        }
    }

    interface Listener {
        fun onTapSkip()
        fun onTapSettings()
    }
}
