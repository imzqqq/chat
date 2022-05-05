package com.imzqqq.app.features.home

import android.content.Context
import android.content.pm.ShortcutManager
import android.os.Build
import androidx.core.content.getSystemService
import androidx.core.content.pm.ShortcutManagerCompat
import com.imzqqq.app.R
import com.imzqqq.app.core.di.ActiveSessionHolder
import com.imzqqq.app.core.dispatchers.CoroutineDispatchers
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.features.pin.PinCodeStore
import com.imzqqq.app.features.pin.PinCodeStoreListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import org.matrix.android.sdk.api.session.room.RoomSortOrder
import org.matrix.android.sdk.api.session.room.model.Membership
import org.matrix.android.sdk.api.session.room.model.RoomSummary
import org.matrix.android.sdk.api.session.room.roomSummaryQueryParams
import org.matrix.android.sdk.flow.flow
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class ShortcutsHandler @Inject constructor(
        private val context: Context,
        private val stringProvider: StringProvider,
        private val appDispatchers: CoroutineDispatchers,
        private val shortcutCreator: ShortcutCreator,
        private val activeSessionHolder: ActiveSessionHolder,
        private val pinCodeStore: PinCodeStore
) : PinCodeStoreListener {

    private val isRequestPinShortcutSupported = ShortcutManagerCompat.isRequestPinShortcutSupported(context)
    private val maxShortcutCountPerActivity = ShortcutManagerCompat.getMaxShortcutCountPerActivity(context)

    // Value will be set correctly if necessary
    private var hasPinCode = AtomicBoolean(true)

    fun observeRoomsAndBuildShortcuts(coroutineScope: CoroutineScope): Job {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
            // No op
            return Job()
        }
        hasPinCode.set(pinCodeStore.getEncodedPin() != null)
        val session = activeSessionHolder.getSafeActiveSession() ?: return Job()
        return session.flow().liveRoomSummaries(
                roomSummaryQueryParams {
                    memberships = listOf(Membership.JOIN)
                },
                sortOrder = RoomSortOrder.PRIORITY_AND_ACTIVITY
        )
                .onStart { pinCodeStore.addListener(this@ShortcutsHandler) }
                .onCompletion { pinCodeStore.removeListener(this@ShortcutsHandler) }
                .onEach { rooms ->
                    // Remove dead shortcuts (i.e. deleted rooms)
                    removeDeadShortcuts(rooms.map { it.roomId })

                    // Create shortcuts
                    createShortcuts(rooms)
                }
                .flowOn(appDispatchers.computation)
                .launchIn(coroutineScope)
    }

    private fun removeDeadShortcuts(roomIds: List<String>) {
        val deadShortcutIds = ShortcutManagerCompat.getShortcuts(context, ShortcutManagerCompat.FLAG_MATCH_DYNAMIC)
                .map { it.id }
                .filter { !roomIds.contains(it) }

        if (deadShortcutIds.isNotEmpty()) {
            Timber.d("Removing shortcut(s) $deadShortcutIds")
            ShortcutManagerCompat.removeLongLivedShortcuts(context, deadShortcutIds)
            if (isRequestPinShortcutSupported) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    ShortcutManagerCompat.disableShortcuts(
                            context,
                            deadShortcutIds,
                            stringProvider.getString(R.string.shortcut_disabled_reason_room_left)
                    )
                }
            }
        }
    }

    private fun createShortcuts(rooms: List<RoomSummary>) {
        if (hasPinCode.get()) {
            // No shortcut in this case (privacy)
            ShortcutManagerCompat.removeAllDynamicShortcuts(context)
        } else {
            val shortcuts = rooms
                    .take(maxShortcutCountPerActivity)
                    .mapIndexed { index, room ->
                        shortcutCreator.create(room, index)
                    }

            shortcuts.forEach { shortcut ->
                ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
            }
        }
    }

    fun clearShortcuts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
            // No op
            return
        }

        // according to Android documentation
        // removeLongLivedShortcuts for API 29 and lower should behave like removeDynamicShortcuts(Context, List)
        // getDynamicShortcuts: returns all dynamic shortcuts from the app.
        val shortcuts = ShortcutManagerCompat.getDynamicShortcuts(context).map { it.id }
        ShortcutManagerCompat.removeLongLivedShortcuts(context, shortcuts)

        // We can only disabled pinned shortcuts with the API, but at least it will prevent the crash
        if (isRequestPinShortcutSupported) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                context.getSystemService<ShortcutManager>()
                        ?.pinnedShortcuts
                        ?.takeIf { it.isNotEmpty() }
                        ?.map { pinnedShortcut -> pinnedShortcut.id }
                        ?.let { shortcutIdsToDisable ->
                            ShortcutManagerCompat.disableShortcuts(
                                    context,
                                    shortcutIdsToDisable,
                                    stringProvider.getString(R.string.shortcut_disabled_reason_sign_out)
                            )
                        }
            }
        }
    }

    override fun onPinSetUpChange(isConfigured: Boolean) {
        hasPinCode.set(isConfigured)
        if (isConfigured) {
            // Remove shortcuts immediately
            ShortcutManagerCompat.removeAllDynamicShortcuts(context)
        }
        // Else shortcut will be created next time any room summary is updated, or
        // next time the app is started which is acceptable
    }
}
