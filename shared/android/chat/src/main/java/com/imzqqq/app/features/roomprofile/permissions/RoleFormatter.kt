package com.imzqqq.app.features.roomprofile.permissions

import com.imzqqq.app.R
import com.imzqqq.app.core.resources.StringProvider
import org.matrix.android.sdk.api.session.room.powerlevels.Role
import javax.inject.Inject

class RoleFormatter @Inject constructor(
        private val stringProvider: StringProvider
) {
    fun format(role: Role): String {
        return when (role) {
            Role.Admin     -> stringProvider.getString(R.string.power_level_admin)
            Role.Moderator -> stringProvider.getString(R.string.power_level_moderator)
            Role.Default   -> stringProvider.getString(R.string.power_level_default)
            is Role.Custom -> stringProvider.getString(R.string.power_level_custom, role.value)
        }
    }
}
