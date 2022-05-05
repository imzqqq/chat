package com.imzqqq.app.core.pushers

import android.content.Context
import com.imzqqq.app.R
import com.imzqqq.app.core.di.ActiveSessionHolder
import com.imzqqq.app.core.resources.AppNameProvider
import com.imzqqq.app.core.resources.LocaleProvider
import com.imzqqq.app.core.resources.StringProvider
import org.matrix.android.sdk.api.session.pushers.PushersService
import java.util.UUID
import javax.inject.Inject
import kotlin.math.abs

private const val DEFAULT_PUSHER_FILE_TAG = "mobile"

class PushersManager @Inject constructor(
        private val activeSessionHolder: ActiveSessionHolder,
        private val localeProvider: LocaleProvider,
        private val stringProvider: StringProvider,
        private val appNameProvider: AppNameProvider
) {
    suspend fun testPush(context: Context) {
        val currentSession = activeSessionHolder.getActiveSession()

        currentSession.testPush(
                UPHelper.getPushGateway(context)!!,
                getPusherAppId(context),
                UPHelper.getUpEndpoint(context)!!,
                TEST_EVENT_ID
        )
    }

    fun enqueueRegisterPusher(
            context: Context,
            pushKey: String,
            gateway: String
    ): UUID {
        val currentSession = activeSessionHolder.getActiveSession()

        return currentSession.enqueueAddHttpPusher(
                createHttpPusher(
                        pushKey,
                        gateway,
                        getPusherAppId(context)
                )
        )
    }

    suspend fun registerPusher(
            context: Context,
            pushKey: String,
            gateway: String
    ) {
        val currentSession = activeSessionHolder.getActiveSession()
        currentSession.addHttpPusher(
                createHttpPusher(
                        pushKey,
                        gateway,
                        getPusherAppId(context)
                )
        )
    }

    private fun createHttpPusher(
            pushKey: String,
            gateway: String,
            deviceId: String
    ) = PushersService.HttpPusher(
            pushKey,
            deviceId,
            profileTag = DEFAULT_PUSHER_FILE_TAG + "_" + abs(activeSessionHolder.getActiveSession().myUserId.hashCode()),
            localeProvider.current().language,
            appNameProvider.getAppName(),
            activeSessionHolder.getActiveSession().sessionParams.deviceId ?: "MOBILE",
            gateway,
            append = false,
            withEventIdOnly = true
    )

    suspend fun registerEmailForPush(email: String) {
        val currentSession = activeSessionHolder.getActiveSession()
        val appName = appNameProvider.getAppName()
        currentSession.addEmailPusher(
                email = email,
                lang = localeProvider.current().language,
                emailBranding = appName,
                appDisplayName = appName,
                deviceDisplayName = currentSession.sessionParams.deviceId ?: "MOBILE"
        )
    }

    suspend fun unregisterEmailPusher(email: String) {
        val currentSession = activeSessionHolder.getSafeActiveSession() ?: return
        currentSession.removeEmailPusher(email)
    }

    suspend fun unregisterPusher(context: Context, pushKey: String) {
        val currentSession = activeSessionHolder.getSafeActiveSession() ?: return
        currentSession.removeHttpPusher(pushKey, getPusherAppId(context))
    }

    private fun getPusherAppId(context: Context) : String {
        return if (UPHelper.isEmbeddedDistributor(context)) {
            stringProvider.getString(R.string.pusher_app_id)
        } else {
            stringProvider.getString(R.string.up_pusher_app_id)
        }
    }

    companion object {
        const val TEST_EVENT_ID = "\$THIS_IS_A_FAKE_EVENT_ID"
    }
}
