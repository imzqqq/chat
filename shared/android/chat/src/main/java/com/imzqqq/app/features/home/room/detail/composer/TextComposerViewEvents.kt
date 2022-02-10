package com.imzqqq.app.features.home.room.detail.composer

import androidx.annotation.StringRes
import com.imzqqq.app.core.platform.VectorViewEvents
import com.imzqqq.app.features.command.Command

sealed class TextComposerViewEvents : VectorViewEvents {

    data class AnimateSendButtonVisibility(val isVisible: Boolean, val isActive: Boolean) : TextComposerViewEvents()

    data class ShowMessage(val message: String) : TextComposerViewEvents()

    abstract class SendMessageResult : TextComposerViewEvents()

    object MessageSent : SendMessageResult()
    data class JoinRoomCommandSuccess(val roomId: String) : SendMessageResult()
    class SlashCommandError(val command: Command) : SendMessageResult()
    class SlashCommandUnknown(val command: String) : SendMessageResult()
    object SlashCommandLoading : SendMessageResult()
    data class SlashCommandResultOk(@StringRes val messageRes: Int? = null) : SendMessageResult()
    class SlashCommandResultError(val throwable: Throwable) : SendMessageResult()

    data class OpenRoomMemberProfile(val userId: String) : TextComposerViewEvents()

    // TODO Remove
    object SlashCommandNotImplemented : SendMessageResult()

    data class ShowRoomUpgradeDialog(val newVersion: String, val isPublic: Boolean) : TextComposerViewEvents()
}
