package com.imzqqq.app.core.ui.bottomsheet

import com.imzqqq.app.core.epoxy.bottomsheet.BottomSheetRadioActionItem_
import com.imzqqq.app.core.platform.VectorSharedAction

/**
 * Parent class for a bottom sheet action
 */
open class BottomSheetGenericRadioAction(
        open val title: CharSequence?,
        open val description: String? = null,
        open val isSelected: Boolean
) : VectorSharedAction {

    fun toRadioBottomSheetItem(): BottomSheetRadioActionItem_ {
        return BottomSheetRadioActionItem_().also {
            it.id("action_$title")
            it.title(title)
            it.selected(isSelected)
            it.description(description)
        }
    }
}
