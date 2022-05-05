package com.imzqqq.app.features.roomprofile.uploads.files

import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.epoxy.VisibilityState
import com.imzqqq.app.R
import com.imzqqq.app.core.date.DateFormatKind
import com.imzqqq.app.core.date.VectorDateFormatter
import com.imzqqq.app.core.epoxy.loadingItem
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.features.roomprofile.uploads.RoomUploadsViewState
import org.matrix.android.sdk.api.session.room.uploads.UploadEvent
import javax.inject.Inject

class UploadsFileController @Inject constructor(
        private val stringProvider: StringProvider,
        private val dateFormatter: VectorDateFormatter
) : TypedEpoxyController<RoomUploadsViewState>() {

    interface Listener {
        fun loadMore()
        fun onOpenClicked(uploadEvent: UploadEvent)
        fun onDownloadClicked(uploadEvent: UploadEvent)
        fun onShareClicked(uploadEvent: UploadEvent)
    }

    var listener: Listener? = null

    private var idx = 0

    init {
        setData(null)
    }

    override fun buildModels(data: RoomUploadsViewState?) {
        data ?: return
        val host = this

        buildFileItems(data.fileEvents)

        if (data.hasMore) {
            loadingItem {
                // Always use a different id, because we can be notified several times of visibility state changed
                id("loadMore${host.idx++}")
                onVisibilityStateChanged { _, _, visibilityState ->
                    if (visibilityState == VisibilityState.VISIBLE) {
                        host.listener?.loadMore()
                    }
                }
            }
        }
    }

    private fun buildFileItems(fileEvents: List<UploadEvent>) {
        val host = this
        fileEvents.forEach { uploadEvent ->
            uploadsFileItem {
                id(uploadEvent.eventId)
                title(uploadEvent.contentWithAttachmentContent.body)
                subtitle(host.stringProvider.getString(R.string.uploads_files_subtitle,
                        uploadEvent.senderInfo.disambiguatedDisplayName,
                        host.dateFormatter.format(uploadEvent.root.originServerTs, DateFormatKind.DEFAULT_DATE_AND_TIME)))
                listener(object : UploadsFileItem.Listener {
                    override fun onItemClicked() {
                        host.listener?.onOpenClicked(uploadEvent)
                    }

                    override fun onDownloadClicked() {
                        host.listener?.onDownloadClicked(uploadEvent)
                    }

                    override fun onShareClicked() {
                        host.listener?.onShareClicked(uploadEvent)
                    }
                })
            }
        }
    }
}
