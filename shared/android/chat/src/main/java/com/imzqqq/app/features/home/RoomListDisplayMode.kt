package com.imzqqq.app.features.home

import androidx.annotation.StringRes
import com.imzqqq.app.R

enum class RoomListDisplayMode(@StringRes val titleRes: Int) {
    ALL(R.string.bottom_action_all),
    NOTIFICATIONS(R.string.bottom_action_notification),
    PEOPLE(R.string.bottom_action_people_x),
    ROOMS(R.string.bottom_action_rooms),
    FILTERED(/* Not used */ 0)
}
