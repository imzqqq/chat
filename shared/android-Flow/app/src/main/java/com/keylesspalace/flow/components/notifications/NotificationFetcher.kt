package com.keylesspalace.flow.components.notifications

import android.util.Log
import com.keylesspalace.flow.db.AccountEntity
import com.keylesspalace.flow.db.AccountManager
import com.keylesspalace.flow.entity.Marker
import com.keylesspalace.flow.entity.Notification
import com.keylesspalace.flow.network.FlowApi
import com.keylesspalace.flow.util.isLessThan
import javax.inject.Inject

class NotificationFetcher @Inject constructor(
    private val flowApi: FlowApi,
    private val accountManager: AccountManager,
    private val notifier: Notifier
) {
    fun fetchAndShow() {
        for (account in accountManager.getAllAccountsOrderedByActive()) {
            if (account.notificationsEnabled) {
                try {
                    val notifications = fetchNotifications(account)
                    notifications.forEachIndexed { index, notification ->
                        notifier.show(notification, account, index == 0)
                    }
                    accountManager.saveAccount(account)
                } catch (e: Exception) {
                    Log.w(TAG, "Error while fetching notifications", e)
                }
            }
        }
    }

    private fun fetchNotifications(account: AccountEntity): MutableList<Notification> {
        val authHeader = String.format("Bearer %s", account.accessToken)
        // We fetch marker to not load/show notifications which user has already seen
        val marker = fetchMarker(authHeader, account)
        if (marker != null && account.lastNotificationId.isLessThan(marker.lastReadId)) {
            account.lastNotificationId = marker.lastReadId
        }
        Log.d(TAG, "getting Notifications for " + account.fullName)
        val notifications = flowApi.notificationsWithAuth(
            authHeader,
            account.domain,
            account.lastNotificationId
        ).blockingGet()

        val newId = account.lastNotificationId
        var newestId = ""
        val result = mutableListOf<Notification>()
        for (notification in notifications.reversed()) {
            val currentId = notification.id
            if (newestId.isLessThan(currentId)) {
                newestId = currentId
                account.lastNotificationId = currentId
            }
            if (newId.isLessThan(currentId)) {
                result.add(notification)
            }
        }
        return result
    }

    private fun fetchMarker(authHeader: String, account: AccountEntity): Marker? {
        return try {
            val allMarkers = flowApi.markersWithAuth(
                authHeader,
                account.domain,
                listOf("notifications")
            ).blockingGet()
            val notificationMarker = allMarkers["notifications"]
            Log.d(TAG, "Fetched marker: $notificationMarker")
            notificationMarker
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch marker", e)
            null
        }
    }

    companion object {
        const val TAG = "NotificationFetcher"
    }
}
