package com.imzqqq.app.features.settings.devtools

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.imzqqq.app.core.ui.list.GenericItem_
import com.imzqqq.app.core.utils.createUIHandler
import me.gujun.android.span.span
import org.matrix.android.sdk.internal.crypto.OutgoingRoomKeyRequest
import javax.inject.Inject

class OutgoingKeyRequestPagedController @Inject constructor() : PagedListEpoxyController<OutgoingRoomKeyRequest>(
        // Important it must match the PageList builder notify Looper
        modelBuildingHandler = createUIHandler()
) {

    interface InteractionListener {
        // fun didTap(data: UserAccountData)
    }

    var interactionListener: InteractionListener? = null

    override fun buildItemModel(currentPosition: Int, item: OutgoingRoomKeyRequest?): EpoxyModel<*> {
        val roomKeyRequest = item ?: return GenericItem_().apply { id(currentPosition) }

        return GenericItem_().apply {
            id(roomKeyRequest.requestId)
            title(roomKeyRequest.requestId)
            description(
                    span {
                        span("roomId: ") {
                            textStyle = "bold"
                        }
                        +"${roomKeyRequest.roomId}"

                        span("\nsessionId: ") {
                            textStyle = "bold"
                        }
                        +"${roomKeyRequest.sessionId}"
                        span("\nstate: ") {
                            textStyle = "bold"
                        }
                        +roomKeyRequest.state.name
                    }
            )
        }
    }
}
