package org.matrix.android.sdk.api.session.presence

import org.matrix.android.sdk.api.session.presence.model.PresenceEnum
import org.matrix.android.sdk.api.session.presence.model.UserPresence

/**
 * This interface defines methods for handling user presence information.
 */
interface PresenceService {
    /**
     * Update the presence status for the current user
     * @param presence the new presence state
     * @param statusMsg the status message to attach to this state
     */
    suspend fun setMyPresence(presence: PresenceEnum, statusMsg: String? = null)

    /**
     * Fetch the given user's presence state.
     * @param userId the userId whose presence state to get.
     */
    suspend fun fetchPresence(userId: String): UserPresence

    // TODO Add live data (of Flow) of the presence of a userId
}
