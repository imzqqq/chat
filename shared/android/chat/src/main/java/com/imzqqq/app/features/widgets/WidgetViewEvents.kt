package com.imzqqq.app.features.widgets

import com.imzqqq.app.core.platform.VectorViewEvents
import org.matrix.android.sdk.api.session.events.model.Content

sealed class WidgetViewEvents : VectorViewEvents {
    data class Failure(val throwable: Throwable) : WidgetViewEvents()
    data class Close(val content: Content? = null) : WidgetViewEvents()
    data class DisplayIntegrationManager(val integId: String?, val integType: String?) : WidgetViewEvents()
    data class OnURLFormatted(val formattedURL: String) : WidgetViewEvents()
    data class DisplayTerms(val url: String, val token: String) : WidgetViewEvents()
}
