package com.imzqqq.app.features.notifications

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.pushrules.PushEvents
import org.matrix.android.sdk.api.pushrules.PushRuleService
import org.matrix.android.sdk.api.pushrules.getActions
import org.matrix.android.sdk.api.session.Session
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PushRuleTriggerListener @Inject constructor(
        private val resolver: NotifiableEventResolver,
        private val notificationDrawerManager: NotificationDrawerManager
) : PushRuleService.PushRuleListener {

    private var session: Session? = null
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob())

    override fun onEvents(pushEvents: PushEvents) {
        scope.launch {
            session?.let { session ->
                val notifiableEvents = createNotifiableEvents(pushEvents, session)
                notificationDrawerManager.updateEvents { queuedEvents ->
                    notifiableEvents.forEach { notifiableEvent ->
                        queuedEvents.onNotifiableEventReceived(notifiableEvent)
                    }
                    queuedEvents.syncRoomEvents(roomsLeft = pushEvents.roomsLeft, roomsJoined = pushEvents.roomsJoined)
                    queuedEvents.markRedacted(pushEvents.redactedEventIds)
                }
            } ?: Timber.e("Called without active session")
        }
    }

    private suspend fun createNotifiableEvents(pushEvents: PushEvents, session: Session): List<NotifiableEvent> {
        return pushEvents.matchedEvents.mapNotNull { (event, pushRule) ->
            Timber.v("Push rule match for event ${event.eventId}")
            val action = pushRule.getActions().toNotificationAction()
            if (action.shouldNotify) {
                resolver.resolveEvent(event, session, isNoisy = !action.soundName.isNullOrBlank())
            } else {
                Timber.v("Matched push rule is set to not notify")
                null
            }
        }
    }

    fun startWithSession(session: Session) {
        if (this.session != null) {
            stop()
        }
        this.session = session
        session.addPushRuleListener(this)
    }

    fun stop() {
        scope.coroutineContext.cancelChildren(CancellationException("PushRuleTriggerListener stopping"))
        session?.removePushRuleListener(this)
        session = null
        notificationDrawerManager.clearAllEvents()
    }
}
