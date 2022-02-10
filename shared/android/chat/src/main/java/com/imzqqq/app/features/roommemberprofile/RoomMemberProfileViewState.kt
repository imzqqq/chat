package com.imzqqq.app.features.roommemberprofile

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import org.matrix.android.sdk.api.session.crypto.crosssigning.MXCrossSigningInfo
import org.matrix.android.sdk.api.session.room.model.Membership
import org.matrix.android.sdk.api.session.room.model.PowerLevelsContent
import org.matrix.android.sdk.api.util.MatrixItem

data class RoomMemberProfileViewState(
        val userId: String,
        val roomId: String?,
        val isSpace: Boolean = false,
        val showAsMember: Boolean = false,
        val isMine: Boolean = false,
        val isIgnored: Async<Boolean> = Uninitialized,
        val isRoomEncrypted: Boolean = false,
        val powerLevelsContent: PowerLevelsContent? = null,
        val userPowerLevelString: Async<String> = Uninitialized,
        val userMatrixItem: Async<MatrixItem> = Uninitialized,
        val userMXCrossSigningInfo: MXCrossSigningInfo? = null,
        val allDevicesAreTrusted: Boolean = false,
        val allDevicesAreCrossSignedTrusted: Boolean = false,
        val asyncMembership: Async<Membership> = Uninitialized,
        val hasReadReceipt: Boolean = false,
        val actionPermissions: ActionPermissions = ActionPermissions()
) : MavericksState {

    constructor(args: RoomMemberProfileArgs) : this(userId = args.userId, roomId = args.roomId)
}

data class ActionPermissions(
        val canKick: Boolean = false,
        val canBan: Boolean = false,
        val canInvite: Boolean = false,
        val canEditPowerLevel: Boolean = false
)
