package com.imzqqq.app.features.ui

import com.imzqqq.app.features.home.RoomListDisplayMode

/**
 * This interface is used to persist UI state across application restart
 */
interface UiStateRepository {

    /**
     * Reset all the saved data
     */
    fun reset()

    fun getDisplayMode(): RoomListDisplayMode

    fun storeDisplayMode(displayMode: RoomListDisplayMode)

    // TODO Handle SharedPreference per session in a better way, also to cleanup when login out
    fun storeSelectedSpace(spaceId: String?, sessionId: String)
    fun storeSelectedGroup(groupId: String?, sessionId: String)

    fun storeGroupingMethod(isSpace: Boolean, sessionId: String)

    fun getSelectedSpace(sessionId: String): String?
    fun getSelectedGroup(sessionId: String): String?
    fun isGroupingMethodSpace(sessionId: String): Boolean

    fun setCustomRoomDirectoryHomeservers(sessionId: String, servers: Set<String>)
    fun getCustomRoomDirectoryHomeservers(sessionId: String): Set<String>
}
