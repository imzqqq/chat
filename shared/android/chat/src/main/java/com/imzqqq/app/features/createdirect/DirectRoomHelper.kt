package com.imzqqq.app.features.createdirect

import com.imzqqq.app.features.raw.wellknown.getElementWellknown
import com.imzqqq.app.features.raw.wellknown.isE2EByDefault
import org.matrix.android.sdk.api.extensions.tryOrNull
import org.matrix.android.sdk.api.raw.RawService
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.room.model.create.CreateRoomParams
import javax.inject.Inject

class DirectRoomHelper @Inject constructor(
        private val rawService: RawService,
        private val session: Session
) {

    suspend fun ensureDMExists(userId: String): String {
        val existingRoomId = tryOrNull { session.getExistingDirectRoomWithUser(userId) }
        val roomId: String
        if (existingRoomId != null) {
            roomId = existingRoomId
        } else {
            val adminE2EByDefault = rawService.getElementWellknown(session.sessionParams)
                    ?.isE2EByDefault()
                    ?: true

            val roomParams = CreateRoomParams().apply {
                invitedUserIds.add(userId)
                setDirectMessage()
                enableEncryptionIfInvitedUsersSupportIt = adminE2EByDefault
            }
            roomId = session.createRoom(roomParams)
        }
        return roomId
    }
}
