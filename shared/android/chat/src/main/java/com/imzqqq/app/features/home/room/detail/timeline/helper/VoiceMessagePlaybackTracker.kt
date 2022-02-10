package com.imzqqq.app.features.home.room.detail.timeline.helper

import android.os.Handler
import android.os.Looper
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class VoiceMessagePlaybackTracker @Inject constructor() {

    private val mainHandler = Handler(Looper.getMainLooper())
    private val listeners = mutableMapOf<String, Listener>()
    private val states = mutableMapOf<String, Listener.State>()

    fun track(id: String, listener: Listener) {
        listeners[id] = listener

        val currentState = states[id] ?: Listener.State.Idle
        mainHandler.post {
            listener.onUpdate(currentState)
        }
    }

    fun unTrack(id: String) {
        listeners.remove(id)
    }

    fun makeAllPlaybacksIdle() {
        listeners.keys.forEach { key ->
            setState(key, Listener.State.Idle)
        }
    }

    /**
     * Set state and notify the listeners
     */
    private fun setState(key: String, state: Listener.State) {
        states[key] = state
        mainHandler.post {
            listeners[key]?.onUpdate(state)
        }
    }

    fun startPlayback(id: String) {
        val currentPlaybackTime = getPlaybackTime(id)
        val currentState = Listener.State.Playing(currentPlaybackTime)
        setState(id, currentState)
        // Pause any active playback
        states
                .filter { it.key != id }
                .keys
                .forEach { key ->
                    val state = states[key]
                    if (state is Listener.State.Playing) {
                        // Paused(state.playbackTime) state should also be considered later.
                        setState(key, Listener.State.Idle)
                    }
                }
    }

    fun pausePlayback(id: String) {
        val currentPlaybackTime = getPlaybackTime(id)
        setState(id, Listener.State.Paused(currentPlaybackTime))
    }

    fun stopPlayback(id: String) {
        setState(id, Listener.State.Idle)
    }

    fun updateCurrentPlaybackTime(id: String, time: Int) {
        setState(id, Listener.State.Playing(time))
    }

    fun updateCurrentRecording(id: String, amplitudeList: List<Int>) {
        setState(id, Listener.State.Recording(amplitudeList))
    }

    fun getPlaybackState(id: String) = states[id]

    fun getPlaybackTime(id: String): Int {
        return when (val state = states[id]) {
            is Listener.State.Playing -> state.playbackTime
            is Listener.State.Paused  -> state.playbackTime
            /* Listener.State.Idle, */
            else                      -> 0
        }
    }

    fun clear() {
        listeners.forEach {
            it.value.onUpdate(Listener.State.Idle)
        }
        listeners.clear()
        states.clear()
    }

    companion object {
        const val RECORDING_ID = "RECORDING_ID"
    }

    interface Listener {

        fun onUpdate(state: State)

        sealed class State {
            object Idle : State()
            data class Playing(val playbackTime: Int) : State()
            data class Paused(val playbackTime: Int) : State()
            data class Recording(val amplitudeList: List<Int>) : State()
        }
    }
}
