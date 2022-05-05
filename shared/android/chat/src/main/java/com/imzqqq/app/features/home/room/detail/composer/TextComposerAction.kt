package com.imzqqq.app.features.home.room.detail.composer

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class TextComposerAction : VectorViewModelAction {
    data class SaveDraft(val draft: String) : TextComposerAction()
    data class SendMessage(val text: CharSequence, val autoMarkdown: Boolean) : TextComposerAction()
    data class EnterEditMode(val eventId: String, val text: String) : TextComposerAction()
    data class EnterQuoteMode(val eventId: String, val text: String) : TextComposerAction()
    data class EnterReplyMode(val eventId: String, val text: String) : TextComposerAction()
    data class EnterRegularMode(val text: String, val fromSharing: Boolean) : TextComposerAction()
    data class UserIsTyping(val isTyping: Boolean) : TextComposerAction()
    data class OnTextChanged(val text: CharSequence) : TextComposerAction()
    data class OnVoiceRecordingStateChanged(val isRecording: Boolean) : TextComposerAction()
}
