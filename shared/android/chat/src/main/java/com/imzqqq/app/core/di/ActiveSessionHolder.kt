package com.imzqqq.app.core.di

import arrow.core.Option
import com.imzqqq.app.ActiveSessionDataSource
import com.imzqqq.app.core.services.GuardServiceStarter
import com.imzqqq.app.features.call.webrtc.WebRtcCallManager
import com.imzqqq.app.features.crypto.keysrequest.KeyRequestHandler
import com.imzqqq.app.features.crypto.verification.IncomingVerificationRequestHandler
import com.imzqqq.app.features.notifications.PushRuleTriggerListener
import com.imzqqq.app.features.session.SessionListener
import org.matrix.android.sdk.api.session.Session
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActiveSessionHolder @Inject constructor(private val sessionObservableStore: ActiveSessionDataSource,
                                              private val keyRequestHandler: KeyRequestHandler,
                                              private val incomingVerificationRequestHandler: IncomingVerificationRequestHandler,
                                              private val callManager: WebRtcCallManager,
                                              private val pushRuleTriggerListener: PushRuleTriggerListener,
                                              private val sessionListener: SessionListener,
                                              private val imageManager: ImageManager,
                                              private val guardServiceStarter: GuardServiceStarter
) {

    private var activeSession: AtomicReference<Session?> = AtomicReference()

    fun setActiveSession(session: Session) {
        Timber.w("setActiveSession of ${session.myUserId}")
        activeSession.set(session)
        sessionObservableStore.post(Option.just(session))

        keyRequestHandler.start(session)
        incomingVerificationRequestHandler.start(session)
        session.addListener(sessionListener)
        pushRuleTriggerListener.startWithSession(session)
        session.callSignalingService().addCallListener(callManager)
        imageManager.onSessionStarted(session)
        guardServiceStarter.start()
    }

    fun clearActiveSession() {
        // Do some cleanup first
        getSafeActiveSession()?.let {
            Timber.w("clearActiveSession of ${it.myUserId}")
            it.callSignalingService().removeCallListener(callManager)
            it.removeListener(sessionListener)
        }

        activeSession.set(null)
        sessionObservableStore.post(Option.empty())

        keyRequestHandler.stop()
        incomingVerificationRequestHandler.stop()
        pushRuleTriggerListener.stop()
    }

    fun hasActiveSession(): Boolean {
        return activeSession.get() != null
    }

    fun getSafeActiveSession(): Session? {
        return activeSession.get()
    }

    fun getActiveSession(): Session {
        return activeSession.get()
                ?: throw IllegalStateException("You should authenticate before using this")
    }

    // TODO: Stop sync ?
//    fun switchToSession(sessionParams: SessionParams) {
//        val newActiveSession = authenticationService.getSession(sessionParams)
//        activeSession.set(newActiveSession)
//    }
}
