package com.imzqqq.app.features.settings.troubleshoot

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import com.imzqqq.app.R
import com.imzqqq.app.core.di.ActiveSessionHolder
import com.imzqqq.app.core.error.ErrorFormatter
import com.imzqqq.app.core.pushers.PushersManager
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.features.session.coroutineScope
import com.imzqqq.app.core.pushers.UPHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.matrix.android.sdk.api.session.pushers.PushGatewayFailure
import javax.inject.Inject

/**
 * Test Push by asking the Push Gateway to send a Push back
 */
class TestPushFromPushGateway @Inject constructor(private val context: FragmentActivity,
                                                  private val stringProvider: StringProvider,
                                                  private val errorFormatter: ErrorFormatter,
                                                  private val pushersManager: PushersManager,
                                                  private val activeSessionHolder: ActiveSessionHolder) :
    TroubleshootTest(R.string.settings_troubleshoot_test_push_loop_title) {

    private var action: Job? = null
    private var pushReceived: Boolean = false

    override fun perform(activityResultLauncher: ActivityResultLauncher<Intent>) {
        pushReceived = false
        UPHelper.getUpEndpoint(context) ?: run {
            status = TestStatus.FAILED
            return
        }
        action = activeSessionHolder.getActiveSession().coroutineScope.launch {
            val result = runCatching { pushersManager.testPush(context) }

            withContext(Dispatchers.Main) {
                status = result
                        .fold(
                                {
                                    if (pushReceived) {
                                        // Push already received (race condition)
                                        description = stringProvider.getString(R.string.settings_troubleshoot_test_push_loop_success)
                                        TestStatus.SUCCESS
                                    } else {
                                        // Wait for the push to be received
                                        description = stringProvider.getString(R.string.settings_troubleshoot_test_push_loop_waiting_for_push)
                                        TestStatus.RUNNING
                                    }
                                },
                                {
                                    description = if (it is PushGatewayFailure.PusherRejected) {
                                        stringProvider.getString(R.string.settings_troubleshoot_test_push_loop_failed)
                                    } else {
                                        errorFormatter.toHumanReadable(it)
                                    }
                                    TestStatus.FAILED
                                }
                        )
            }
        }
    }

    override fun onPushReceived() {
        pushReceived = true
        description = stringProvider.getString(R.string.settings_troubleshoot_test_push_loop_success)
        status = TestStatus.SUCCESS
    }

    override fun cancel() {
        action?.cancel()
    }
}
