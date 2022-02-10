package com.imzqqq.app.features.voice

sealed class VoiceFailure(cause: Throwable? = null) : Throwable(cause = cause) {
    data class UnableToPlay(val throwable: Throwable) : VoiceFailure(throwable)
    data class UnableToRecord(val throwable: Throwable) : VoiceFailure(throwable)
}
