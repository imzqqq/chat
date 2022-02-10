package com.imzqqq.app.features.roomprofile.alias.detail

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.imzqqq.app.R
import com.imzqqq.app.core.platform.VectorSharedAction

sealed class RoomAliasBottomSheetSharedAction(
        @StringRes val titleRes: Int,
        @DrawableRes val iconResId: Int = 0,
        val destructive: Boolean = false) :
    VectorSharedAction {

    data class ShareAlias(val matrixTo: String) : RoomAliasBottomSheetSharedAction(
            R.string.share,
            R.drawable.ic_material_share
    )

    data class PublishAlias(val alias: String) : RoomAliasBottomSheetSharedAction(
            R.string.room_alias_action_publish
    )

    data class UnPublishAlias(val alias: String) : RoomAliasBottomSheetSharedAction(
            R.string.room_alias_action_unpublish
    )

    data class DeleteAlias(val alias: String) : RoomAliasBottomSheetSharedAction(
            R.string.delete,
            R.drawable.ic_trash_24,
            true
    )

    data class SetMainAlias(val alias: String) : RoomAliasBottomSheetSharedAction(
            R.string.room_settings_set_main_address
    )

    object UnsetMainAlias : RoomAliasBottomSheetSharedAction(
            R.string.room_settings_unset_main_address
    )
}
