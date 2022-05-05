package com.imzqqq.app.features.roomdirectory

data class RoomDirectoryServer(
        val serverName: String,

        /**
         * True if this is the current user server
         */
        val isUserServer: Boolean,

        /**
         * True if manually added, so it can be removed by the user
         */
        val isManuallyAdded: Boolean,

        /**
         * Supported protocols
         * TODO Rename RoomDirectoryData to RoomDirectoryProtocols
         */
        val protocols: List<RoomDirectoryData>
)
