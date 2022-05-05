package com.imzqqq.app.features.roomprofile.settings.joinrule

import com.imzqqq.app.core.ui.bottomsheet.BottomSheetGenericRadioAction
import org.matrix.android.sdk.api.session.room.model.RoomJoinRules

class RoomJoinRuleRadioAction(
        val roomJoinRule: RoomJoinRules,
        title: CharSequence,
        description: String,
        isSelected: Boolean
) : BottomSheetGenericRadioAction(
        title = title,
        isSelected = isSelected,
        description = description
)
