package org.matrix.android.sdk.api

interface RoomDisplayNameFallbackProvider {
    fun getNameForRoomInvite(): String
    fun getNameForEmptyRoom(isDirect: Boolean, leftMemberNames: List<String>): String
    fun getNameFor1member(name: String): String
    fun getNameFor2members(name1: String, name2: String): String
    fun getNameFor3members(name1: String, name2: String, name3: String): String
    fun getNameFor4members(name1: String, name2: String, name3: String, name4: String): String
    fun getNameFor4membersAndMore(name1: String, name2: String, name3: String, remainingCount: Int): String
}
