package com.imzqqq.app.flow.components.notifications

import android.util.Log
import com.imzqqq.app.flow.db.AccountEntity
import com.imzqqq.app.flow.db.AccountManager
import com.imzqqq.app.flow.entity.Marker
import com.imzqqq.app.flow.entity.Notification
import com.imzqqq.app.flow.network.MastodonApi
import com.imzqqq.app.flow.util.isLessThan
import timber.log.Timber
import javax.inject.Inject

class NotificationFetcher @Inject constructor(
        private val mastodonApi: MastodonApi,
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
                    Timber.tag(TAG).w(e, "Error while fetching notifications")
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
        Timber.d("getting Notifications for " + account.fullName)
        val notifications = mastodonApi.notificationsWithAuth(
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
            val allMarkers = mastodonApi.markersWithAuth(
                authHeader,
                account.domain,
                listOf("notifications")
            ).blockingGet()
            val notificationMarker = allMarkers["notifications"]
            Timber.d("Fetched marker: $notificationMarker")
            notificationMarker
        } catch (e: Exception) {
            Timber.e(e, "Failed to fetch marker")
            null
        }
    }

    companion object {
        const val TAG = "NotificationFetcher"
    }
}
