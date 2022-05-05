package com.imzqqq.app.features.roomdirectory

/**
 * This class describes a rooms directory server protocol.
 */
data class RoomDirectoryData(
        /**
         * The server name (might be null)
         * Set null when the server is the current user's homeserver.
         */
        val homeServer: String? = null,

        /**
         * The display name (the server description)
         */
        val displayName: String = MATRIX_PROTOCOL_NAME,

        /**
         * the avatar url
         */
        val avatarUrl: String? = null,

        /**
         * The third party server identifier
         */
        val thirdPartyInstanceId: String? = null,

        /**
         * Tell if all the federated servers must be included
         */
        val includeAllNetworks: Boolean = false
) {

    companion object {
        const val MATRIX_PROTOCOL_NAME = "Matrix"
    }
}
