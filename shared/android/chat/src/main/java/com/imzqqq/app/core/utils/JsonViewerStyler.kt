package com.imzqqq.app.core.utils

import com.imzqqq.app.R
import com.imzqqq.app.core.resources.ColorProvider
import org.billcarsonfr.jsonviewer.JSonViewerStyleProvider

fun createJSonViewerStyleProvider(colorProvider: ColorProvider): JSonViewerStyleProvider {
    return JSonViewerStyleProvider(
            keyColor = colorProvider.getColorFromAttribute(R.attr.colorPrimary),
            secondaryColor = colorProvider.getColorFromAttribute(R.attr.vctr_content_secondary),
            stringColor = colorProvider.getColorFromAttribute(R.attr.vctr_notice_text_color),
            baseColor = colorProvider.getColorFromAttribute(R.attr.vctr_content_primary),
            booleanColor = colorProvider.getColorFromAttribute(R.attr.vctr_notice_text_color),
            numberColor = colorProvider.getColorFromAttribute(R.attr.vctr_notice_text_color)
    )
}
