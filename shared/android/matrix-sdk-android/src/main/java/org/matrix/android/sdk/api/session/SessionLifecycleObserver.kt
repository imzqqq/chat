package org.matrix.android.sdk.api.session

import androidx.annotation.MainThread

/**
 * This defines methods associated with some lifecycle events of a session.
 */
interface SessionLifecycleObserver {
    /*
    Called when the session is opened
     */
    @MainThread
    fun onSessionStarted(session: Session) {
        // noop
    }

    /*
    Called when the session is cleared
     */
    @MainThread
    fun onClearCache(session: Session) {
        // noop
    }

    /*
    Called when the session is closed
     */
    @MainThread
    fun onSessionStopped(session: Session) {
        // noop
    }
}
