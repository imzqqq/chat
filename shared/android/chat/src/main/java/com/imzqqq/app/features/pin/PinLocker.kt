package com.imzqqq.app.features.pin

import android.os.SystemClock
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imzqqq.app.features.settings.VectorPreferences
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

// 2 minutes, when enabled
private const val PERIOD_OF_GRACE_IN_MS = 2 * 60 * 1000L

/**
 * This class is responsible for keeping the status of locking
 * It automatically locks when entering background/foreground with a grace period.
 * You can force to unlock with unlock method, use it whenever the pin code has been validated.
 */
@Singleton
class PinLocker @Inject constructor(
        private val pinCodeStore: PinCodeStore,
        private val vectorPreferences: VectorPreferences
) : DefaultLifecycleObserver {

    enum class State {
        // App is locked, can be unlock
        LOCKED,

        // App is unlocked, the app can be used
        UNLOCKED
    }

    private val liveState = MutableLiveData<State>()

    private var shouldBeLocked = true
    private var entersBackgroundTs = 0L

    fun getLiveState(): LiveData<State> {
        return liveState
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    private fun computeState() {
        GlobalScope.launch {
            val state = if (shouldBeLocked && pinCodeStore.hasEncodedPin()) {
                State.LOCKED
            } else {
                State.UNLOCKED
            }
                    .also { Timber.v("New state: $it") }

            if (liveState.value != state) {
                liveState.postValue(state)
            }
        }
    }

    fun unlock() {
        Timber.v("Unlock app")
        shouldBeLocked = false
        computeState()
    }

    fun screenIsOff() {
        shouldBeLocked = true
        computeState()
    }

    override fun onResume(owner: LifecycleOwner) {
        val timeElapsedSinceBackground = SystemClock.elapsedRealtime() - entersBackgroundTs
        shouldBeLocked = shouldBeLocked || timeElapsedSinceBackground >= getGracePeriod()
        Timber.v("App enters foreground after $timeElapsedSinceBackground ms spent in background shouldBeLocked: $shouldBeLocked")
        computeState()
    }

    override fun onPause(owner: LifecycleOwner) {
        Timber.v("App enters background")
        entersBackgroundTs = SystemClock.elapsedRealtime()
    }

    private fun getGracePeriod(): Long {
        return if (vectorPreferences.useGracePeriod()) {
            PERIOD_OF_GRACE_IN_MS
        } else {
            0L
        }
    }
}
