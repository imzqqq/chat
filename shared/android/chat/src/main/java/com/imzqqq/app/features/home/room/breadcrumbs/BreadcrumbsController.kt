package com.imzqqq.app.features.home.room.breadcrumbs

import com.airbnb.epoxy.EpoxyController
import com.imzqqq.app.core.epoxy.zeroItem
import com.imzqqq.app.features.home.AvatarRenderer
import org.matrix.android.sdk.api.util.toMatrixItem
import javax.inject.Inject

class BreadcrumbsController @Inject constructor(
        private val avatarRenderer: AvatarRenderer
) : EpoxyController() {

    var listener: Listener? = null

    private var viewState: BreadcrumbsViewState? = null

    init {
        // We are requesting a model build directly as the first build of epoxy is on the main thread.
        // It avoids to build the whole list of breadcrumbs on the main thread.
        requestModelBuild()
    }

    fun update(viewState: BreadcrumbsViewState) {
        this.viewState = viewState
        requestModelBuild()
    }

    override fun buildModels() {
        val safeViewState = viewState ?: return
        val host = this
        // Add a ZeroItem to avoid automatic scroll when the breadcrumbs are updated from another client
        zeroItem {
            id("top")
        }

        // An empty breadcrumbs list can only be temporary because when entering in a room,
        // this one is added to the breadcrumbs
        safeViewState.asyncBreadcrumbs.invoke()
                ?.forEach { roomSummary ->
                    breadcrumbsItem {
                        id(roomSummary.roomId)
                        hasTypingUsers(roomSummary.typingUsers.isNotEmpty())
                        avatarRenderer(host.avatarRenderer)
                        matrixItem(roomSummary.toMatrixItem())
                        unreadNotificationCount(roomSummary.notificationCount)
                        markedUnread(roomSummary.markedUnread)
                        showHighlighted(roomSummary.highlightCount > 0)
                        hasUnreadMessage(roomSummary.scIsUnread())
                        hasDraft(roomSummary.userDrafts.isNotEmpty())
                        itemClickListener {
                            host.listener?.onBreadcrumbClicked(roomSummary.roomId)
                        }
                    }
                }
    }

    interface Listener {
        fun onBreadcrumbClicked(roomId: String)
    }
}
