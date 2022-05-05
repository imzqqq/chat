package com.imzqqq.app.features.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imzqqq.app.core.extensions.postLiveEvent
import com.imzqqq.app.core.utils.LiveEvent
import com.imzqqq.app.features.call.vectorCallService
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.failure.GlobalError
import org.matrix.android.sdk.api.session.Session
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionListener @Inject constructor() : Session.Listener {

    private val _globalErrorLiveData = MutableLiveData<LiveEvent<GlobalError>>()
    val globalErrorLiveData: LiveData<LiveEvent<GlobalError>>
        get() = _globalErrorLiveData

    override fun onGlobalError(session: Session, globalError: GlobalError) {
        _globalErrorLiveData.postLiveEvent(globalError)
    }

    override fun onNewInvitedRoom(session: Session, roomId: String) {
        session.coroutineScope.launch {
            session.vectorCallService.userMapper.onNewInvitedRoom(roomId)
        }
    }

    override fun onSessionStopped(session: Session) {
        session.coroutineScope.coroutineContext.cancelChildren()
    }

    override fun onClearCache(session: Session) {
        session.coroutineScope.coroutineContext.cancelChildren()
    }
}
