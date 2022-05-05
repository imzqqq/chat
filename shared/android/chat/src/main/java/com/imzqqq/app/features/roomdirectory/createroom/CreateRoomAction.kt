package com.imzqqq.app.features.roomdirectory.createroom

import android.net.Uri
import com.imzqqq.app.core.platform.VectorViewModelAction
import org.matrix.android.sdk.api.session.room.model.RoomJoinRules

sealed class CreateRoomAction : VectorViewModelAction {
    data class SetAvatar(val imageUri: Uri?) : CreateRoomAction()
    data class SetName(val name: String) : CreateRoomAction()
    data class SetTopic(val topic: String) : CreateRoomAction()
    data class SetVisibility(val rule: RoomJoinRules) : CreateRoomAction()
    data class SetRoomAliasLocalPart(val aliasLocalPart: String) : CreateRoomAction()
    data class SetIsEncrypted(val isEncrypted: Boolean) : CreateRoomAction()

    object ToggleShowAdvanced : CreateRoomAction()
    data class DisableFederation(val disableFederation: Boolean) : CreateRoomAction()

    object Create : CreateRoomAction()
    object Reset : CreateRoomAction()
}
