package com.imzqqq.app.features.home.room.detail

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import org.matrix.android.sdk.api.extensions.orFalse
import org.matrix.android.sdk.api.session.events.model.Event
import org.matrix.android.sdk.api.session.initsync.SyncStatusService
import org.matrix.android.sdk.api.session.room.members.ChangeMembershipState
import org.matrix.android.sdk.api.session.room.model.RoomMemberSummary
import org.matrix.android.sdk.api.session.room.model.RoomSummary
import org.matrix.android.sdk.api.session.room.powerlevels.PowerLevelsHelper
import org.matrix.android.sdk.api.session.sync.SyncState
import org.matrix.android.sdk.api.session.widgets.model.Widget
import org.matrix.android.sdk.api.session.widgets.model.WidgetType

sealed class UnreadState {
    object Unknown : UnreadState()
    object HasNoUnread : UnreadState()
    data class ReadMarkerNotLoaded(val readMarkerId: String) : UnreadState()
    data class HasUnread(val firstUnreadEventId: String) : UnreadState()
}

data class JitsiState(
        val hasJoined: Boolean = false,
        // Not null if we have an active jitsi widget on the room
        val confId: String? = null,
        val widgetId: String? = null,
        val deleteWidgetInProgress: Boolean = false
)

data class RoomDetailViewState(
        val roomId: String,
        val eventId: String?,
        val openAtFirstUnread: Boolean? = null,
        val myRoomMember: Async<RoomMemberSummary> = Uninitialized,
        val asyncInviter: Async<RoomMemberSummary> = Uninitialized,
        val asyncRoomSummary: Async<RoomSummary> = Uninitialized,
        val powerLevelsHelper: PowerLevelsHelper? = null,
        val activeRoomWidgets: Async<List<Widget>> = Uninitialized,
        val formattedTypingUsers: String? = null,
        val tombstoneEvent: Event? = null,
        val joinUpgradedRoomAsync: Async<String> = Uninitialized,
        val syncState: SyncState = SyncState.Idle,
        val incrementalSyncStatus: SyncStatusService.Status.IncrementalSyncStatus = SyncStatusService.Status.IncrementalSyncIdle,
        val pushCounter: Int = 0,
        val highlightedEventId: String? = null,
        val unreadState: UnreadState = UnreadState.Unknown,
        val canShowJumpToReadMarker: Boolean = true,
        val changeMembershipState: ChangeMembershipState = ChangeMembershipState.Unknown,
        val canInvite: Boolean = true,
        val isAllowedToManageWidgets: Boolean = false,
        val isAllowedToStartWebRTCCall: Boolean = true,
        val hasFailedSending: Boolean = false,
        val jitsiState: JitsiState = JitsiState()
) : MavericksState {

    constructor(args: RoomDetailArgs) : this(
            roomId = args.roomId,
            eventId = args.eventId,
            // Also highlight the target event, if any
            highlightedEventId = args.eventId,
            openAtFirstUnread = args.openAtFirstUnread
    )

    fun isWebRTCCallOptionAvailable() = (asyncRoomSummary.invoke()?.joinedMembersCount ?: 0) <= 2

    // This checks directly on the active room widgets.
    // It can differs for a short period of time on the JitsiState as its computed async.
    fun hasActiveJitsiWidget() = activeRoomWidgets()?.any { it.type == WidgetType.Jitsi && it.isActive }.orFalse()

    fun isDm() = asyncRoomSummary()?.isDirect == true

    fun isPublic() = asyncRoomSummary()?.isPublic == true
}
