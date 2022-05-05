package com.imzqqq.app.features.roomprofile.settings.historyvisibility

import com.imzqqq.app.R
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.bottomsheet.BottomSheetGenericController
import com.imzqqq.app.features.home.room.detail.timeline.format.RoomHistoryVisibilityFormatter
import org.matrix.android.sdk.api.session.room.model.RoomHistoryVisibility
import javax.inject.Inject

class RoomHistoryVisibilityController @Inject constructor(
        private val historyVisibilityFormatter: RoomHistoryVisibilityFormatter,
        private val stringProvider: StringProvider
) : BottomSheetGenericController<RoomHistoryVisibilityState, RoomHistoryVisibilityRadioAction>() {

    override fun getTitle() = stringProvider.getString(R.string.room_settings_room_read_history_rules_pref_dialog_title)

    override fun getSubTitle() = stringProvider.getString(R.string.room_settings_room_read_history_dialog_subtitle)

    override fun getActions(state: RoomHistoryVisibilityState): List<RoomHistoryVisibilityRadioAction> {
        return listOf(
                RoomHistoryVisibility.WORLD_READABLE,
                RoomHistoryVisibility.SHARED,
                RoomHistoryVisibility.INVITED,
                RoomHistoryVisibility.JOINED
        )
                .map { roomHistoryVisibility ->
                    RoomHistoryVisibilityRadioAction(
                            roomHistoryVisibility = roomHistoryVisibility,
                            title = historyVisibilityFormatter.getSetting(roomHistoryVisibility),
                            isSelected = roomHistoryVisibility == state.currentRoomHistoryVisibility
                    )
                }
    }
}
