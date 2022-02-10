package com.imzqqq.app.features.home

import androidx.lifecycle.asFlow
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.AppStateHandler
import com.imzqqq.app.RoomGroupingMethod
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.extensions.singletonEntryPoint
import com.imzqqq.app.core.flow.throttleFirst
import com.imzqqq.app.core.platform.VectorViewModel
import com.imzqqq.app.features.call.dialpad.DialPadLookup
import com.imzqqq.app.features.call.lookup.CallProtocolsChecker
import com.imzqqq.app.features.call.webrtc.WebRtcCallManager
import com.imzqqq.app.features.createdirect.DirectRoomHelper
import com.imzqqq.app.features.invite.AutoAcceptInvites
import com.imzqqq.app.features.invite.showInvites
import com.imzqqq.app.features.settings.VectorDataStore
import com.imzqqq.app.features.ui.UiStateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.query.ActiveSpaceFilter
import org.matrix.android.sdk.api.query.RoomCategoryFilter
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.initsync.SyncStatusService
import org.matrix.android.sdk.api.session.room.RoomSortOrder
import org.matrix.android.sdk.api.session.room.model.Membership
import org.matrix.android.sdk.api.session.room.roomSummaryQueryParams
import org.matrix.android.sdk.api.util.toMatrixItem
import org.matrix.android.sdk.flow.flow
import timber.log.Timber

/**
 * View model used to update the home bottom bar notification counts, observe the sync state and
 * change the selected room list view
 */
class HomeDetailViewModel @AssistedInject constructor(@Assisted initialState: HomeDetailViewState,
                                                      private val session: Session,
                                                      private val uiStateRepository: UiStateRepository,
                                                      private val vectorDataStore: VectorDataStore,
                                                      private val callManager: WebRtcCallManager,
                                                      private val directRoomHelper: DirectRoomHelper,
                                                      private val appStateHandler: AppStateHandler,
                                                      private val autoAcceptInvites: AutoAcceptInvites) :
        VectorViewModel<HomeDetailViewState, HomeDetailAction, HomeDetailViewEvents>(initialState),
        CallProtocolsChecker.Listener {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<HomeDetailViewModel, HomeDetailViewState> {
        override fun create(initialState: HomeDetailViewState): HomeDetailViewModel
    }

    companion object : MavericksViewModelFactory<HomeDetailViewModel, HomeDetailViewState> by hiltMavericksViewModelFactory() {

        override fun initialState(viewModelContext: ViewModelContext): HomeDetailViewState {
            val uiStateRepository = viewModelContext.activity.singletonEntryPoint().uiStateRepository()
            return HomeDetailViewState(
                    currentTab = HomeTab.RoomList(uiStateRepository.getDisplayMode())
            )
        }
    }

    init {
        observeSyncState()
        observeRoomGroupingMethod()
        observeRoomSummaries()
        updateShowDialPadTab()
        observeDataStore()
        callManager.addProtocolsCheckerListener(this)
        session.flow().liveUser(session.myUserId).execute {
            copy(
                    myMatrixItem = it.invoke()?.getOrNull()?.toMatrixItem()
            )
        }
    }

    private fun observeDataStore() {
        viewModelScope.launch {
            vectorDataStore.pushCounterFlow.collect { nbOfPush ->
                setState {
                    copy(
                            pushCounter = nbOfPush
                    )
                }
            }
        }
    }

    override fun handle(action: HomeDetailAction) {
        when (action) {
            is HomeDetailAction.SwitchTab                -> handleSwitchTab(action)
            HomeDetailAction.MarkAllRoomsRead            -> handleMarkAllRoomsRead()
            is HomeDetailAction.StartCallWithPhoneNumber -> handleStartCallWithPhoneNumber(action)
        }
    }

    private fun handleStartCallWithPhoneNumber(action: HomeDetailAction.StartCallWithPhoneNumber) {
        viewModelScope.launch {
            try {
                _viewEvents.post(HomeDetailViewEvents.Loading)
                val result = DialPadLookup(session, callManager, directRoomHelper).lookupPhoneNumber(action.phoneNumber)
                callManager.startOutgoingCall(result.roomId, result.userId, isVideoCall = false)
                _viewEvents.post(HomeDetailViewEvents.CallStarted)
            } catch (failure: Throwable) {
                _viewEvents.post(HomeDetailViewEvents.FailToCall(failure))
            }
        }
    }

    private fun handleSwitchTab(action: HomeDetailAction.SwitchTab) = withState { state ->
        if (state.currentTab != action.tab) {
            setState {
                copy(currentTab = action.tab)
            }
            if (action.tab is HomeTab.RoomList) {
                uiStateRepository.storeDisplayMode(action.tab.displayMode)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        callManager.removeProtocolsCheckerListener(this)
    }

    override fun onPSTNSupportUpdated() {
        updateShowDialPadTab()
    }

    private fun updateShowDialPadTab() {
        setState {
            copy(showDialPadTab = callManager.supportsPSTNProtocol)
        }
    }

    // PRIVATE METHODS *****************************************************************************

    private fun handleMarkAllRoomsRead() = withState { _ ->
        // questionable to use viewmodelscope
        viewModelScope.launch(Dispatchers.Default) {
            val roomIds = session.getRoomSummaries(
                    roomSummaryQueryParams {
                        memberships = listOf(Membership.JOIN)
                        roomCategoryFilter = RoomCategoryFilter.ONLY_WITH_NOTIFICATIONS
                    }
            )
                    .map { it.roomId }
            try {
                session.markAllAsRead(roomIds)
            } catch (failure: Throwable) {
                Timber.d(failure, "Failed to mark all as read")
            }
        }
    }

    private fun observeSyncState() {
        session.flow()
                .liveSyncState()
                .setOnEach { syncState ->
                    copy(syncState = syncState)
                }

        session.getSyncStatusLive()
                .asFlow()
                .filterIsInstance<SyncStatusService.Status.IncrementalSyncStatus>()
                .setOnEach {
                    copy(incrementalSyncStatus = it)
                }
    }

    private fun observeRoomGroupingMethod() {
        appStateHandler.selectedRoomGroupingObservable
                .setOnEach {
                    copy(
                            roomGroupingMethod = it.orNull() ?: RoomGroupingMethod.BySpace(null)
                    )
                }
    }

    private fun observeRoomSummaries() {
        appStateHandler.selectedRoomGroupingObservable.distinctUntilChanged().flatMapLatest {
            // we use it as a trigger to all changes in room, but do not really load
            // the actual models
            session.getPagedRoomSummariesLive(
                    roomSummaryQueryParams {
                        memberships = Membership.activeMemberships()
                    },
                    sortOrder = RoomSortOrder.NONE
            ).asFlow()
        }
                .throttleFirst(300)
                .onEach {
                    when (val groupingMethod = appStateHandler.getCurrentRoomGroupingMethod()) {
                        is RoomGroupingMethod.ByLegacyGroup -> {
                            // TODO!!
                        }
                        is RoomGroupingMethod.BySpace       -> {
                            val activeSpaceRoomId = groupingMethod.spaceSummary?.roomId
                            var dmInvites = 0
                            var roomsInvite = 0
                            if (autoAcceptInvites.showInvites()) {
                                dmInvites = session.getRoomSummaries(
                                        roomSummaryQueryParams {
                                            memberships = listOf(Membership.INVITE)
                                            roomCategoryFilter = RoomCategoryFilter.ONLY_DM
                                            activeSpaceFilter = activeSpaceRoomId?.let { ActiveSpaceFilter.ActiveSpace(it) } ?: ActiveSpaceFilter.None
                                        }
                                ).size

                                roomsInvite = session.getRoomSummaries(
                                        roomSummaryQueryParams {
                                            memberships = listOf(Membership.INVITE)
                                            roomCategoryFilter = RoomCategoryFilter.ONLY_ROOMS
                                            activeSpaceFilter = ActiveSpaceFilter.ActiveSpace(groupingMethod.spaceSummary?.roomId)
                                        }
                                ).size
                            }

                            val dmRooms = session.getNotificationCountForRooms(
                                    roomSummaryQueryParams {
                                        memberships = listOf(Membership.JOIN)
                                        roomCategoryFilter = RoomCategoryFilter.ONLY_DM
                                        activeSpaceFilter = activeSpaceRoomId?.let { ActiveSpaceFilter.ActiveSpace(it) } ?: ActiveSpaceFilter.None
                                    }
                            )

                            val otherRooms = session.getNotificationCountForRooms(
                                    roomSummaryQueryParams {
                                        memberships = listOf(Membership.JOIN)
                                        roomCategoryFilter = RoomCategoryFilter.ONLY_ROOMS
                                        activeSpaceFilter = ActiveSpaceFilter.ActiveSpace(groupingMethod.spaceSummary?.roomId)
                                    }
                            )

                            setState {
                                copy(
                                        notificationCountCatchup = dmRooms.totalCount + otherRooms.totalCount + roomsInvite + dmInvites,
                                        notificationHighlightCatchup = dmRooms.isHighlight || otherRooms.isHighlight || (dmInvites + roomsInvite) > 0,
                                        notificationCountPeople = dmRooms.totalCount + dmInvites,
                                        notificationHighlightPeople = dmRooms.isHighlight || dmInvites > 0,
                                        notificationCountRooms = otherRooms.totalCount + roomsInvite,
                                        notificationHighlightRooms = otherRooms.isHighlight || roomsInvite > 0,
                                        hasUnreadMessages = dmRooms.totalCount + otherRooms.totalCount > 0
                                )
                            }
                        }
                    }
                }
                .launchIn(viewModelScope)
    }
}
