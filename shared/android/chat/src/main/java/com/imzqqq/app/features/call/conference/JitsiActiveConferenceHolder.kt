package com.imzqqq.app.features.call.conference

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import org.matrix.android.sdk.api.extensions.orFalse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JitsiActiveConferenceHolder @Inject constructor(context: Context) {

    private var activeConference: String? = null

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(ConferenceEventObserver(context, this::onBroadcastEvent))
    }

    fun isJoined(confId: String?): Boolean {
        return confId != null && activeConference?.endsWith(confId).orFalse()
    }

    private fun onBroadcastEvent(conferenceEvent: ConferenceEvent) {
        when (conferenceEvent) {
            is ConferenceEvent.Joined     -> activeConference = conferenceEvent.extractConferenceUrl()
            is ConferenceEvent.Terminated -> activeConference = null
            else                          -> Unit
        }
    }
}
