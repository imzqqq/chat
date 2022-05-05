package org.matrix.android.sdk.api.session.presence.model

data class UserPresence(
        val lastActiveAgo: Long? = null,
        val statusMessage: String? = null,
        val isCurrentlyActive: Boolean? = null,
        val presence: PresenceEnum = PresenceEnum.OFFLINE
)
