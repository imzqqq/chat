package com.imzqqq.app.features.roomdirectory

/**
 * Join state of a room
 */
enum class JoinState {
    NOT_JOINED,
    JOINING,
    JOINING_ERROR,

    // Room is joined and this is confirmed by the sync
    JOINED
}
