package com.imzqqq.app.features.spaces.create

import android.net.Uri
import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class CreateSpaceAction : VectorViewModelAction {
    data class SetRoomType(val type: SpaceType) : CreateSpaceAction()
    data class NameChanged(val name: String) : CreateSpaceAction()
    data class TopicChanged(val topic: String) : CreateSpaceAction()
    data class SpaceAliasChanged(val aliasLocalPart: String) : CreateSpaceAction()
    data class SetAvatar(val uri: Uri?) : CreateSpaceAction()
    object OnBackPressed : CreateSpaceAction()
    object NextFromDetails : CreateSpaceAction()
    object NextFromDefaultRooms : CreateSpaceAction()
    object NextFromAdd3pid : CreateSpaceAction()
    data class DefaultRoomNameChanged(val index: Int, val name: String) : CreateSpaceAction()
    data class DefaultInvite3pidChanged(val index: Int, val email: String) : CreateSpaceAction()
    data class SetSpaceTopology(val topology: SpaceTopology) : CreateSpaceAction()
}
