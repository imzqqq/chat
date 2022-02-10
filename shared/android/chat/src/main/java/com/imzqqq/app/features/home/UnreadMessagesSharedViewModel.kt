package com.imzqqq.app.features.home

import androidx.lifecycle.asFlow
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.AppStateHandler
import com.imzqqq.app.RoomGroupingMethod
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.flow.throttleFirst
import com.imzqqq.app.core.platform.EmptyAction
import com.imzqqq.app.core.platform.EmptyViewEvents
import com.imzqqq.app.core.platform.VectorViewModel
import com.imzqqq.app.features.invite.AutoAcceptInvites
import com.imzqqq.app.features.settings.VectorPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import org.matrix.android.sdk.api.query.ActiveSpaceFilter
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.room.RoomSortOrder
import org.matrix.android.sdk.api.session.room.model.Membership
import org.matrix.android.sdk.api.session.room.roomSummaryQueryParams
import org.matrix.android.sdk.api.session.room.spaceSummaryQueryParams
import org.matrix.android.sdk.api.session.room.summary.RoomAggregateNotificationCount

data class UnreadMessagesState(
        val homeSpaceUnread: RoomAggregateNotificationCount = RoomAggregateNotificationCount(0, 0, 0),
        val otherSpacesUnread: RoomAggregateNotificationCount = RoomAggregateNotificationCount(0, 0, 0)
) : MavericksState

data class CountInfo(
        val homeCount: RoomAggregateNotificationCount,
        val otherCount: RoomAggregateNotificationCount
)

class UnreadMessagesSharedViewModel @AssistedInject constructor(@Assisted initialState: UnreadMessagesState,
                                                                session: Session,
                                                                private val vectorPreferences: VectorPreferences,
                                                                //appStateHandler: AppStateHandler,
                                                                private val autoAcceptInvites: AutoAcceptInvites) :
        VectorViewModel<UnreadMessagesState, EmptyAction, EmptyViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<UnreadMessagesSharedViewModel, UnreadMessagesState> {
        override fun create(initialState: UnreadMessagesState): UnreadMessagesSharedViewModel
    }

    companion object : MavericksViewModelFactory<UnreadMessagesSharedViewModel, UnreadMessagesState> by hiltMavericksViewModelFactory()

    override fun handle(action: EmptyAction) {}

    init {

        session.getPagedRoomSummariesLive(
                roomSummaryQueryParams {
                    this.memberships = listOf(Membership.JOIN)
                    this.activeSpaceFilter = ActiveSpaceFilter.ActiveSpace(null)
                }, sortOrder = RoomSortOrder.NONE
        ).asFlow()
                .throttleFirst(300)
                .execute {
                    val counts = session.getNotificationCountForRooms(
                            roomSummaryQueryParams {
                                this.memberships = listOf(Membership.JOIN)
                                this.activeSpaceFilter = ActiveSpaceFilter.ActiveSpace(null)
                            }
                    )
                    val invites = if (autoAcceptInvites.hideInvites) {
                        0
                    } else {
                        session.getRoomSummaries(
                                roomSummaryQueryParams {
                                    this.memberships = listOf(Membership.INVITE)
                                    this.activeSpaceFilter = ActiveSpaceFilter.ActiveSpace(null)
                                }
                        ).size
                    }

                    copy(
                            homeSpaceUnread = RoomAggregateNotificationCount(
                                    counts.notificationCount + invites,
                                    highlightCount = counts.highlightCount + invites,
                                    unreadCount = counts.unreadCount
                            )
                    )
                }

        session.getPagedRoomSummariesLive(
                roomSummaryQueryParams {
                    this.memberships = Membership.activeMemberships()
                }, sortOrder = RoomSortOrder.NONE
        ).asFlow()
                .throttleFirst(300)
                .execute {
        /*
        combine(
                appStateHandler.selectedRoomGroupingObservable.distinctUntilChanged(),
                appStateHandler.selectedRoomGroupingObservable.flatMapLatest {
                    session.getPagedRoomSummariesLive(
                            roomSummaryQueryParams {
                                this.memberships = Membership.activeMemberships()
                            }, sortOrder = RoomSortOrder.NONE
                    ).asFlow()
                            .throttleFirst(300)
                }
        ) { groupingMethod, _ ->
            when (groupingMethod.orNull()) {
                is RoomGroupingMethod.ByLegacyGroup -> {
                    // currently not supported
                    CountInfo(
                            RoomAggregateNotificationCount(0, 0, 0),
                            RoomAggregateNotificationCount(0, 0, 0)
                    )
                }
                is RoomGroupingMethod.BySpace       -> {
                    //val selectedSpace = appStateHandler.safeActiveSpaceId()
                    */

                    val inviteCount = if (autoAcceptInvites.hideInvites) {
                        0
                    } else {
                        session.getRoomSummaries(
                                roomSummaryQueryParams { this.memberships = listOf(Membership.INVITE) }
                        ).size
                    }

                    val spacesShowAllRoomsInHome = vectorPreferences.prefSpacesShowAllRoomInHome()

                    val spaceInviteCount = if (autoAcceptInvites.hideInvites) {
                        0
                    } else {
                        session.getRoomSummaries(
                                spaceSummaryQueryParams {
                                    this.memberships = listOf(Membership.INVITE)
                                }
                        ).size
                    }

                    val totalCount = session.getNotificationCountForRooms(
                            roomSummaryQueryParams {
                                this.memberships = listOf(Membership.JOIN)
                                this.activeSpaceFilter = ActiveSpaceFilter.ActiveSpace(null).takeIf {
                                    !spacesShowAllRoomsInHome
                                } ?: ActiveSpaceFilter.None
                            }
                    )

                    val counts = RoomAggregateNotificationCount(
                            totalCount.notificationCount + inviteCount,
                            totalCount.highlightCount + inviteCount,
                            totalCount.unreadCount
                    )


                    // SC: count total room notifications for drawer badge, instead of filtering for others like Element does,
                    // to prevent counting rooms multiple times
                    val topLevelTotalCount = if (spacesShowAllRoomsInHome) {
                        totalCount
                    } else {
                        session.getNotificationCountForRooms(
                                roomSummaryQueryParams {
                                    this.memberships = listOf(Membership.JOIN)
                                    this.activeSpaceFilter = ActiveSpaceFilter.None
                                }
                        )
                    }

                    val topLevelCounts = RoomAggregateNotificationCount(
                            topLevelTotalCount.notificationCount + inviteCount + spaceInviteCount,
                            topLevelTotalCount.highlightCount + inviteCount + spaceInviteCount,
                            topLevelTotalCount.unreadCount
                    )

                    copy(
                            homeSpaceUnread = counts,
                            otherSpacesUnread = topLevelCounts
                    )
                    //CountInfo(homeCount = counts, otherCount = topLevelCounts)

                    /*
                    val rootCounts = session.spaceService().getRootSpaceSummaries()
                            .filter {
                                // filter out current selection
                                it.roomId != selectedSpace
                            }

                    CountInfo(
                            homeCount = counts,
                            otherCount = RoomAggregateNotificationCount(
                                    notificationCount = rootCounts.fold(0, { acc, rs -> acc + rs.notificationCount }) +
                                            (counts.notificationCount.takeIf { selectedSpace != null } ?: 0) +
                                            spaceInviteCount,
                                    highlightCount = rootCounts.fold(0, { acc, rs -> acc + rs.highlightCount }) +
                                            (counts.highlightCount.takeIf { selectedSpace != null } ?: 0) +
                                            spaceInviteCount,

                                    unreadCount = rootCounts.fold(0, { acc, rs -> acc + (if (rs.scIsUnread()) 1 else 0) }) +
                                            (counts.unreadCount.takeIf { selectedSpace != null } ?: 0) +
                                            spaceInviteCount
                            )
                    )
                     */
        /*
                }
                null                                -> {
                    CountInfo(
                            RoomAggregateNotificationCount(0, 0, 0),
                            RoomAggregateNotificationCount(0, 0, 0)
                    )
                }
            }
        }
                .flowOn(Dispatchers.Default)
                .execute {
                    copy(
                            homeSpaceUnread = it.invoke()?.homeCount ?: RoomAggregateNotificationCount(0, 0, 0),
                            otherSpacesUnread = it.invoke()?.otherCount ?: RoomAggregateNotificationCount(0, 0, 0)
                    )
        */
                }
    }
}
