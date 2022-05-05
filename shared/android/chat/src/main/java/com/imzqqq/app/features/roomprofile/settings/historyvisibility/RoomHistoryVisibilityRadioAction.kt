package com.imzqqq.app.features.roomprofile.settings.historyvisibility

import com.imzqqq.app.core.ui.bottomsheet.BottomSheetGenericRadioAction
import org.matrix.android.sdk.api.session.room.model.RoomHistoryVisibility

class RoomHistoryVisibilityRadioAction(
        val roomHistoryVisibility: RoomHistoryVisibility,
        title: String?,
        isSelected: Boolean
) : BottomSheetGenericRadioAction(
        title = title,
        isSelected = isSelected
)
