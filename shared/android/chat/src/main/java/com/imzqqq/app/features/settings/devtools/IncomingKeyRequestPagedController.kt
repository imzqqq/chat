package com.imzqqq.app.features.settings.devtools

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.imzqqq.app.core.date.DateFormatKind
import com.imzqqq.app.core.date.VectorDateFormatter
import com.imzqqq.app.core.ui.list.GenericItem_
import com.imzqqq.app.core.utils.createUIHandler
import me.gujun.android.span.span
import org.matrix.android.sdk.internal.crypto.IncomingRoomKeyRequest
import javax.inject.Inject

class IncomingKeyRequestPagedController @Inject constructor(
        private val vectorDateFormatter: VectorDateFormatter
) : PagedListEpoxyController<IncomingRoomKeyRequest>(
        // Important it must match the PageList builder notify Looper
        modelBuildingHandler = createUIHandler()
) {

    interface InteractionListener {
        // fun didTap(data: UserAccountData)
    }

    var interactionListener: InteractionListener? = null

    override fun buildItemModel(currentPosition: Int, item: IncomingRoomKeyRequest?): EpoxyModel<*> {
        val host = this
        val roomKeyRequest = item ?: return GenericItem_().apply { id(currentPosition) }

        return GenericItem_().apply {
            id(roomKeyRequest.requestId)
            title(roomKeyRequest.requestId)
            description(
                    span {
                        span("From: ") {
                            textStyle = "bold"
                        }
                        span("${roomKeyRequest.userId}")
                        +host.vectorDateFormatter.format(roomKeyRequest.localCreationTimestamp, DateFormatKind.DEFAULT_DATE_AND_TIME)
                        span("\nsessionId:") {
                            textStyle = "bold"
                        }
                        +"${roomKeyRequest.requestBody?.sessionId}"
                        span("\nFrom device:") {
                            textStyle = "bold"
                        }
                        +"${roomKeyRequest.deviceId}"
                        span("\nstate: ") {
                            textStyle = "bold"
                        }
                        +roomKeyRequest.state.name
                    }
            )
        }
    }
}
