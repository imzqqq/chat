package com.imzqqq.app.features.call.conference

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import org.matrix.android.sdk.api.session.widgets.model.Widget

data class JitsiCallViewState(
        val roomId: String = "",
        val widgetId: String = "",
        val enableVideo: Boolean = false,
        val widget: Async<Widget> = Uninitialized
) : MavericksState {

    constructor(args: VectorJitsiActivity.Args) : this(
            roomId = args.roomId,
            widgetId = args.widgetId,
            enableVideo = args.enableVideo
    )
}
